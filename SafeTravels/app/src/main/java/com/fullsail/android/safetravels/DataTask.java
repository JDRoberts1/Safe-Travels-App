package com.fullsail.android.safetravels;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.commons.io.IOUtil;

import java.io.IOException;
import java.io.InputStream;

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

        Response response = null;

        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String results = null;

       InputStream iS = null;
       String result = "ERROR: Results not set. . .";

        try {
            if (response != null) {
                iS = response.body().byteStream();
                result = IOUtil.toString(iS, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                iS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.i(TAG, "doInBackground: " + result);
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        mInterface.onPost(s);
    }
}
