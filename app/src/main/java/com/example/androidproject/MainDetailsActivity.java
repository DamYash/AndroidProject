package com.example.androidproject;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

        tvUrl.setOnClickListener(v -> utils.openBrowser(v, articleModel.getUrl()));

        btnOpenInBrowser.setOnClickListener(v -> utils.openBrowser(v, articleModel.getUrl()));

        fabSave.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle(R.string.dialog_save_title);
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.yes, (dialog, id) -> {
                if (GDatabase.getInstance(getBaseContext()).save(articleModel)) {
                    Toast.makeText(v.getContext(), "Success Save To Favorite", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(v.getContext(), "Fail Save To Favorite", Toast.LENGTH_LONG).show();
                }
                dialog.cancel();
            });
            builder.setNegativeButton(R.string.no, (dialog, id) -> dialog.cancel());
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_help) {
            utils.showDialogHelp(
                    this,
                    String.format("%s\n\n%s", getString(R.string.help_save_message), getString(R.string.help_open_message))
            );
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
