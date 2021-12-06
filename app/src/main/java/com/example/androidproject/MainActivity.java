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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        GuardianAsyncTask.Callback, ArticleAdapter.ItemClick {

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private final ArrayList<ArticleModel> articleModels = new ArrayList<>();

    private ArrayAdapter<ArticleModel> arrayAdapter;

    private ProgressBar progressCircular;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        progressCircular = findViewById(R.id.progress_circular);
        FloatingActionButton fabSearch = findViewById(R.id.fab_search);
        ListView listView = findViewById(R.id.list_view);

        setUpToolbar(toolbar);
        setSupportActionBar(toolbar);
        setUpNavigationDrawer(toolbar);

        arrayAdapter = new ArticleAdapter(this, articleModels, this);

        listView.setAdapter(arrayAdapter);

        new GuardianAsyncTask(this).execute();

        fabSearch.setOnClickListener(vFab -> showDialogSearch());
    }

    private void setUpNavigationDrawer(Toolbar toolbar) {
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        DrawerLayout drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.nav_open,
                R.string.nav_close
        );

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            int id = item.getItemId();
            if (id == R.id.menu_favorite) {
                startActivity(new Intent(this, FavoriteActivity.class));
                return true;
            } else if (id == R.id.menu_all) {
                new GuardianAsyncTask(this).execute();
                return true;
            } else if (id == R.id.menu_news) {
                new GuardianAsyncTask(this).execute("&section=news");
            } else if (id == R.id.menu_sport) {
                new GuardianAsyncTask(this).execute("&section=sport");
                return true;
            }
            return false;
        });
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }
    public void setUpToolbar(Toolbar toolbar) {
        toolbar.setTitle(R.string.label_home);

        // set font size subtitle toolbar
        toolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitleAppearance);

        // add version number in subtitle toolbar
        toolbar.setSubtitle(getString(R.string.version) + " " + BuildConfig.VERSION_NAME);
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
                    new GuardianAsyncTask(MainActivity.this).execute("&q=" + query);
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(
                            vSearch.getContext(),
                            vSearch.getContext().getString(R.string.input_text_search),
                            Toast.LENGTH_LONG
                    ).show();
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
            Toast.makeText(getBaseContext(), getString(R.string.message_no_result), Toast.LENGTH_LONG).show();
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
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else if (item.getItemId() == R.id.menu_help) {
            utils.showDialogHelp(
                    this,
                    String.format("%s\n\n%s\n\n%s", getString(R.string.help_item_main), getString(R.string.help_search), getString(R.string.help_drawer))
            );
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
