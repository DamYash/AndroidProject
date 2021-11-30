package com.example.androidproject;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_DATA_ARTICLE = "EXTRA_DATA_ARTICLE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.label_detail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ArticleModel articleModel = getIntent().getParcelableExtra(EXTRA_DATA_ARTICLE);

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvUrl = findViewById(R.id.tv_url);
        TextView tvSelectionName = findViewById(R.id.tv_selection_name);
        AppCompatButton btnOpenInBrowser = findViewById(R.id.btn_open_in_browser);
        FloatingActionButton fabSave = findViewById(R.id.fab_save);
        tvTitle.setText(articleModel.getTitle());
        tvUrl.setText(articleModel.getUrl());
        tvSelectionName.setText(articleModel.getSectionName());


        fabSave.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Are you sure want to save to favorites");
            builder.setCancelable(true);
            builder.setPositiveButton("Yes", (dialog, id) -> {
                if (GDatabase.getInstance(getBaseContext()).save(articleModel)) {
                    Toast.makeText(v.getContext(), "Success Save To Favorite", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(v.getContext(), "Fail Save To Favorite", Toast.LENGTH_LONG).show();
                }
                dialog.cancel();
            });
            builder.setNegativeButton("No", (dialog, id) -> dialog.cancel());
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
}
