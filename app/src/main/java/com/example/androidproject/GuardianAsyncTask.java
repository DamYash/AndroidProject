package com.example.androidproject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

public class GuardianAsyncTask extends AsyncTask<String, ArticleModel, Void> {
    public interface Callback {
        void onPreExecute();

        void onError(Throwable t, String query);

        void onPostExecute();

        void onProgressUpdate(List<ArticleModel> models);
    }

    private final Callback callback;

    private final WeakReference<Context> contextWeakReference;

    private final String ALL = "all";

    private final List<String> queryList = Arrays.asList("news", "sport", ALL);

    @SuppressWarnings("deprecation")
    public GuardianAsyncTask(Context context, Callback callback) {
        this.callback = callback;
        this.contextWeakReference = new WeakReference<>(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... strings) {
        String query = strings.length > 0 ? strings[0] : null;
        try {
            GDatabase db = GDatabase.getInstance(contextWeakReference.get());
            String qs = query != null ? query.split("=")[1] : ALL;
            publishProgress(db.get(qs).toArray(new ArticleModel[0]));
            GuardianResponse guardianResponse = GuardianApi.search(query);
            //old data cleared
            db.deleteWithQuery(qs);
            for (GuardianResult guardianResult : guardianResponse.getResults()) {
                db.save(
                        new ArticleModel(
                                guardianResult.getId(),
                                guardianResult.getWebTitle(),
                                guardianResult.getWebUrl(),
                                guardianResult.getSectionName(),
                                qs,
                                db.isFavorite(guardianResult.getId())
                        )
                );
            }
            publishProgress(db.get(qs).toArray(new ArticleModel[0]));

            if (!queryList.contains(qs)) {
                db.deleteWithQuery(qs);
            }
        } catch (Throwable t) {
            new Handler(Looper.getMainLooper()).post(() -> callback.onError(t, query));
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(ArticleModel... values) {
        super.onProgressUpdate(values);
        callback.onProgressUpdate(Arrays.asList(values));
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        callback.onPostExecute();
    }
}