package com.booklisting.booklisting;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SaherOs on 1/27/2018.
 */

class QueryUtils {


    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {}

    public static List<MyBook> fetchMyBookData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return extractFeatureFromJson(jsonResponse);
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(10000 /* milliseconds */);

            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");

            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static List<MyBook> extractFeatureFromJson(String MyBookJSON) {
        if (TextUtils.isEmpty(MyBookJSON)) {
            return null;
        }
        List<MyBook> myBooks = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(MyBookJSON);
            JSONArray MyBookArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < MyBookArray.length(); i++) {
                JSONObject currentMyBook = MyBookArray.getJSONObject(i);
                    JSONObject properties = currentMyBook.getJSONObject("volumeInfo");

                String title  = properties.getString("title");
                String author = "";
                JSONArray authorsArray = properties.getJSONArray("authors");
                int j = 0;
                while (j < authorsArray.length()-1){
                    author = author + authorsArray.getString(j)+", " ;
                    j++;
                }
                author = author + authorsArray.getString(j);

                MyBook myBook = new MyBook(title, author);
                myBooks.add(myBook);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
            /*for(int i=0 ; i<1 ; i++){
                MyBook myBook = new MyBook("HI", "HI");
                myBooks.add(myBook);
            }*/

        return myBooks;
    }
}
