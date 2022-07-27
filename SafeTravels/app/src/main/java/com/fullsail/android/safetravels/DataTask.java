package com.fullsail.android.safetravels;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.apache.commons.io.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataTask extends AsyncTask<String, String, String> {
    private static final String TAG = "AsyncTask: ";
    final private OnFinished mInterface;

    interface OnFinished{
        void onPost(String result);
    }


    DataTask(OnFinished _Interface){ mInterface = _Interface; }


    @Override
    protected String doInBackground(String... strings) {

        // TODO: PULL DATA FROM API
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://spott.p.rapidapi.com/places/autocomplete?limit=10&skip=0&country=US%2CCA&q=Sea&type=CITY")
                .get()
                .addHeader("X-RapidAPI-Key", "a37b576032msha06820f5d94f387p144c67jsn8b971038de49")
                .addHeader("X-RapidAPI-Host", "spott.p.rapidapi.com")
                .build();


        String results = null;

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.w(TAG, "onFailure: ", e);
            }

            @Override
            public void onResponse(Response response) throws IOException {

                ResponseBody body = response.body();

                String results = body.string();

                Log.i(TAG, "onResponse: " + results);
            }
        });

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        mInterface.onPost(s);
    }
}
