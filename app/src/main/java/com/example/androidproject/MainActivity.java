package com.example.androidproject;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        GuardianAsyncTask.Callback, ArticleAdapter.ItemClick {

    private final ArrayList<ArticleModel> articleModels = new ArrayList<>();

    private ArrayAdapter<ArticleModel> arrayAdapter;

    private ProgressBar progressCircular;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        progressCircular = findViewById(R.id.progress_circular);

        toolbar.setTitle(R.string.label_home);
        setSupportActionBar(toolbar);

        arrayAdapter = new ArticleAdapter(this, articleModels, this);

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(arrayAdapter);

        new GuardianAsyncTask(this).execute();

        FloatingActionButton fabSearch = findViewById(R.id.fab_search);
        fabSearch.setOnClickListener(vFab -> showDialogSearch());
    }

    private void showDialogSearch() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Text");
        View view = getLayoutInflater().inflate(R.layout.search, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        AppCompatButton btnSearch = view.findViewById(R.id.btn_search);
        TextInputEditText tiSearch = view.findViewById(R.id.ti_search);
        btnSearch.setOnClickListener(vSearch -> {
            Editable editable = tiSearch.getText();
            if (editable != null) {
                String query = editable.toString();
                if (!query.isEmpty()) {
                    new GuardianAsyncTask(MainActivity.this).execute(query);
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(vSearch.getContext(), "Please Input Text", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onPreExecute() {
        progressCircular.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(Throwable t, String query) {
        String errorMessage = t.getMessage();
        String message = errorMessage != null ? errorMessage : getBaseContext().getString(R.string.message_error);
        View root = findViewById(R.id.root);
        Snackbar snackbar = Snackbar.make(root, message, Snackbar.LENGTH_LONG);
        snackbar.show();
        snackbar.setAction(R.string.retry, v -> {
            new GuardianAsyncTask(this).execute(query);
            snackbar.dismiss();
        });
    }

    @Override
    public void onPostExecute(@Nullable GuardianResponse response) {
        if (response != null && !response.getResults().isEmpty()) {
            articleModels.clear();
            for (GuardianResult result : response.getResults()) {
                articleModels.add(
                        new ArticleModel(result.getId(), result.getWebTitle(), result.getWebUrl(), result.getSectionName())
                );
            }
            arrayAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getBaseContext(), "No Result", Toast.LENGTH_LONG).show();
        }
        progressCircular.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_favorite) {
            startActivity(new Intent(this, FavoriteActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v, ArticleModel model) {
        Intent intent = new Intent(v.getContext(), MainDetailsActivity.class);
        intent.putExtra(MainDetailsActivity.EXTRA_DATA_ARTICLE, model);
        v.getContext().startActivity(intent);
    }
}
