package com.booklisting.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MyBook>> {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();


    private static final String MYBOOK_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";

    private static final int MYBOOK_LOADER_ID = 1;
    private MyBookAdapter mAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView myBookListView = (ListView) findViewById(R.id.list_of_my_books);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        myBookListView.setEmptyView(mEmptyStateTextView);
        Button myButton = (Button) findViewById(R.id.button_search);
        mAdapter = new MyBookAdapter(this, new ArrayList<MyBook>());
        mEmptyStateTextView.setText(R.string.no_search_text);
        myBookListView.setAdapter(mAdapter);

        final ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(MYBOOK_LOADER_ID, null, this);
        }
        else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    mAdapter.clear();
                    View loadingIndicator = findViewById(R.id.loading_indicator);
                    EditText search_book = (EditText) findViewById(R.id.Search_text);
                    if (TextUtils.isEmpty(search_book.getText().toString().trim())) {
                        loadingIndicator.setVisibility(View.GONE);
                        mEmptyStateTextView.setVisibility(View.VISIBLE);
                        mEmptyStateTextView.setText(R.string.no_search_text);
                    }
                    else {
                        mEmptyStateTextView.setVisibility(View.GONE);
                        loadingIndicator.setVisibility(View.VISIBLE);
                        getLoaderManager().restartLoader(MYBOOK_LOADER_ID, null, MainActivity.this);
                    }
                }
                else {
                    mAdapter.clear();
                    View loadingIndicator = findViewById(R.id.loading_indicator);
                    loadingIndicator.setVisibility(View.GONE);
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                }
            }
        });
    }

    @Override
    public Loader<List<MyBook>> onCreateLoader(int i, Bundle bundle) {

        EditText search_book = (EditText) findViewById(R.id.Search_text);
        String queryBook = search_book.getText().toString().trim();
        URL url = null;
        try {
            url = new URL(MYBOOK_REQUEST_URL + queryBook);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return new MyBookLoader(this, url.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<MyBook>> loader, List<MyBook> myBooks) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mAdapter.clear();
        EditText search_book = (EditText) findViewById(R.id.Search_text);
        if (TextUtils.isEmpty(search_book.getText().toString().trim())) {
            mEmptyStateTextView.setText(R.string.no_search_text);
        }
        else {
        mEmptyStateTextView.setText(R.string.no_books);
        }
        if (myBooks != null && !myBooks.isEmpty()) {
            mAdapter.addAll(myBooks);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<MyBook>> loader) {
        mAdapter.clear();
    }
}
