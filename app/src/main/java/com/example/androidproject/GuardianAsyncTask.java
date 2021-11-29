package com.example.androidproject;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

public class GuardianAsyncTask extends AsyncTask<String, Void, GuardianResponse> {

    public interface Callback {
        void onPreExecute();

        void onError(Throwable t, String query);

        void onPostExecute(@Nullable GuardianResponse guardianResponse);
    }

    private final Callback callback;

    @SuppressWarnings("deprecation")
    public GuardianAsyncTask(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.onPreExecute();
    }

    @Override
    protected GuardianResponse doInBackground(String... strings) {
        String query = strings.length > 0 ? strings[0] : null;
        try {
            return GuardianApi.search(query);
        } catch (Throwable t) {
            new Handler(Looper.getMainLooper()).post(() -> callback.onError(t, query));
            return null;
        }
    }

    @Override
    protected void onPostExecute(GuardianResponse guardianResponse) {
        super.onPostExecute(guardianResponse);
        callback.onPostExecute(guardianResponse);
    }
}
