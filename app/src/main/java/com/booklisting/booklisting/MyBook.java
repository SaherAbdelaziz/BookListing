package com.booklisting.booklisting;

/**
 * Created by SaherOs on 1/27/2018.
 */

public class MyBook {

    private String mTitle;
    private StringBuilder mAuthor;

    public MyBook(String title, StringBuilder author){
        mTitle = title;
        mAuthor = author;
    }


    public String getmTitle() {
        return mTitle;
    }
    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public StringBuilder getmAuthor() {
        return mAuthor;
    }
    public void setmAuthor(StringBuilder mAuthor) {
        this.mAuthor = mAuthor;
    }
}
