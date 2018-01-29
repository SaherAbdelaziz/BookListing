package com.booklisting.booklisting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by SaherOs on 1/27/2018.
 */

public class MyBookAdapter extends ArrayAdapter<MyBook> {

    public MyBookAdapter(Context context, List<MyBook> myBookList) {
        super(context, 0, myBookList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.mybook_list_item, parent, false);
        }

        MyBook currentBook = getItem(position);

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.mybookauthor);
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.mybooktitle);

        authorTextView.setText(currentBook.getmAuthor());
        titleTextView.setText(currentBook.getmTitle());

        return listItemView;
    }
}
