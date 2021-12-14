package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment
        implements GuardianAsyncTask.Callback, ArticleAdapter.ItemClick {

    public static final String KEY_QUERY = "KEY_QUERY";

    public static final String QUERY_SPORT = "&section=sport";

    public static final String QUERY_NEWS = "&section=news";

    private final ArrayList<ArticleModel> articles = new ArrayList<>();

    private ArrayAdapter<ArticleModel> arrayAdapter;

    private ProgressBar progressCircular;

    public static String querySearch(String query) {
        return "&q=" + query;
    }

    public MainFragment() {
        // initial view
        super(R.layout.fragment_main);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressCircular = view.findViewById(R.id.progress_circular);

        arrayAdapter = new ArticleAdapter(requireContext(), articles, this);
        ListView listView = view.findViewById(R.id.list_view);

        listView.setAdapter(arrayAdapter);

        String query = null;
        Bundle bundle = getArguments();
        if (bundle != null) {
            query = bundle.getString(KEY_QUERY);
        }

        new GuardianAsyncTask(getContext(), this).execute(query);
    }

    @Override
    public void onPreExecute() {
        progressCircular.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(Throwable t, String query) {
        String errorMessage = t.getMessage();
        String message = errorMessage != null ? errorMessage : requireContext().getString(R.string.message_error);
        Snackbar snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG);
        snackbar.show();
        snackbar.setAction(R.string.retry, v -> {
            new GuardianAsyncTask(getContext(), this).execute(query);
            snackbar.dismiss();
        });
    }

    @Override
    public void onProgressUpdate(List<ArticleModel> models) {
        if (!models.isEmpty()) {
            progressCircular.setVisibility(View.GONE);
            articles.clear();
            articles.addAll(models);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPostExecute() {
        progressCircular.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v, ArticleModel model) {
        Intent intent = new Intent(v.getContext(), MainDetailsActivity.class);
        intent.putExtra(MainDetailsActivity.EXTRA_DATA_ARTICLE, model);
        v.getContext().startActivity(intent);
    }
}
