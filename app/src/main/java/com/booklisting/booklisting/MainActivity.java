package com.booklisting.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

        myBookListView.setAdapter(mAdapter);



        final ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
        {
            View loadingIndicator = findViewById(R.id.progressBar);
            loadingIndicator.setVisibility(View.VISIBLE);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(MYBOOK_LOADER_ID, null, this);
        }
        else
        {
            View loadingIndicator = findViewById(R.id.progressBar);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    mAdapter.clear();
                    mEmptyStateTextView.setVisibility(View.GONE);
                    View loadingIndicator = findViewById(R.id.progressBar);
                    loadingIndicator.setVisibility(View.VISIBLE);

                    EditText search_book = (EditText) findViewById(R.id.Search_text);
                    if(TextUtils.isEmpty(search_book.getText().toString().trim()))
                    {
                        // If the search is on empty text
                        mEmptyStateTextView.setText(R.string.no_books);
                    }
                    else
                    {
                        getLoaderManager().restartLoader(MYBOOK_LOADER_ID, null, MainActivity.this);
                    }
                }
            }
        });
    }


    @Override
    public Loader<List<MyBook>> onCreateLoader(int i, Bundle bundle) {
        //Uri baseUri = Uri.parse(MYBOOK_REQUEST_URL);
        //Uri.Builder uriBuilder = baseUri.buildUpon();

        EditText search_book = (EditText) findViewById(R.id.Search_text);
        String queryBook = search_book.getText().toString().trim();
        URL url = null;
        try {
            url = new URL(MYBOOK_REQUEST_URL + queryBook);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        //uriBuilder.appendQueryParameter(getString(R.string.q), queryBook);
        //uriBuilder.appendQueryParameter(getString(R.string.max_results), "10");
        return new MyBookLoader(this, url.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<MyBook>> loader, List<MyBook> myBooks) {
        View loadingIndicator = findViewById(R.id.progressBar);
            loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_books);

        mAdapter.clear();

        if (myBooks != null && !myBooks.isEmpty()) {
            mAdapter.addAll(myBooks);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<MyBook>> loader) {
        mAdapter.clear();
    }
}
