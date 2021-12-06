package com.example.androidproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FavoriteDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_DATA_ARTICLE = "EXTRA_DATA_FAVORITE_ARTICLE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_details);

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(R.string.label_favorite_detail);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ArticleModel articleModel = getIntent().getParcelableExtra(EXTRA_DATA_ARTICLE);

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvUrl = findViewById(R.id.tv_url);
        TextView tvSelectionName = findViewById(R.id.tv_selection_name);

        tvTitle.setText(articleModel.getTitle());
        tvUrl.setText(articleModel.getUrl());
        tvSelectionName.setText(articleModel.getSectionName());

        tvUrl.setOnClickListener(v -> utils.openBrowser(v, articleModel.getUrl()));

        AppCompatButton btnOpenInBrowser = findViewById(R.id.btn_open_in_browser);

        btnOpenInBrowser.setOnClickListener(v -> utils.openBrowser(v, articleModel.getUrl()));

        FloatingActionButton fabSave = findViewById(R.id.fab_save);

        fabSave.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle(R.string.dialog_title_delete_favorite);
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.yes, (dialog, id) -> {
                if (GDatabase.getInstance(getBaseContext()).delete(articleModel.getId())) {
                    Toast.makeText(v.getContext(), v.getContext().getString(R.string.success_delete_favorite), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.putExtra(FavoriteActivity.KEY_IS_DELETE, true);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(v.getContext(), v.getContext().getString(R.string.fail_delete_favorite), Toast.LENGTH_LONG).show();
                }
                dialog.cancel();
            });
            builder.setNegativeButton(R.string.no,(dialog, id) -> dialog.cancel());
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
}