package com.example.androidproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity implements ArticleAdapter.ItemClick {

    public static final String KEY_IS_DELETE = "KEY_IS_DELETE";

    private final ArrayList<ArticleModel> articleModels = new ArrayList<>();

    private ArticleAdapter arrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favorite);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.label_favorite);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        articleModels.addAll(GDatabase.getInstance(this).get());
        arrayAdapter = new ArticleAdapter(this, articleModels, this);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View v, ArticleModel model) {
        Intent intent = new Intent(v.getContext(), FavoriteDetailsActivity.class);
        intent.putExtra(FavoriteDetailsActivity.EXTRA_DATA_ARTICLE, model);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null && data.getBooleanExtra(KEY_IS_DELETE, false)) {
            articleModels.clear();
            articleModels.addAll(GDatabase.getInstance(this).get());
            arrayAdapter.notifyDataSetChanged();
        }
    }
}
