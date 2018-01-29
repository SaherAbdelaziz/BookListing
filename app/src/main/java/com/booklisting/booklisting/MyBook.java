package com.booklisting.booklisting;

/**
 * Created by SaherOs on 1/27/2018.
 */

public class MyBook {

    private String mTitle;
    private String mAuthor;

    public MyBook(String title, String author){
        mTitle = title;
        mAuthor = author;
    }


    public String getmTitle() {
        return mTitle;
    }
    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }
    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }
}
