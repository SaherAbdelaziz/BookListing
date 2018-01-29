package com.booklisting.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by SaherOs on 1/27/2018.
 */

public class MyBookLoader extends AsyncTaskLoader<List<MyBook>> {

    private String mUrl;

    public MyBookLoader(Context context , String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<MyBook> loadInBackground() {
        if(mUrl == null){
            return null;
        }

        return QueryUtils.fetchMyBookData(mUrl);
        //return null ;
    }
}
