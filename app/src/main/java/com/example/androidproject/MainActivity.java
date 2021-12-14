package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private final Bundle bundle = new Bundle();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        FloatingActionButton fabSearch = findViewById(R.id.fab_search);

        setUpToolbar(toolbar);
        setSupportActionBar(toolbar);
        setUpNavigationDrawer(toolbar);

        replaceFragment(getSupportFragmentManager(), null);

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
                replaceFragment(getSupportFragmentManager(), null);
                return true;
            } else if (id == R.id.menu_news) {
                replaceFragment(getSupportFragmentManager(), MainFragment.QUERY_NEWS);
            } else if (id == R.id.menu_sport) {
                replaceFragment(getSupportFragmentManager(), MainFragment.QUERY_SPORT);
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
        SharedPreferences preferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        String KEY_LAST_SEARCH = "last_search";
        tiSearch.setText(preferences.getString(KEY_LAST_SEARCH, null));
        btnSearch.setOnClickListener(vSearch -> {
            Editable editable = tiSearch.getText();
            if (editable != null) {
                String query = editable.toString();
                preferences.edit()
                        .putString(KEY_LAST_SEARCH, query)
                        .apply();
                if (!query.isEmpty()) {
                    replaceFragment (getSupportFragmentManager(), MainFragment.querySearch(query));
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

    private void replaceFragment(FragmentManager fragmentManager, @Nullable String query) {
        if (query != null) {
            bundle.putString(MainFragment.KEY_QUERY, query);
        } else {
            bundle.remove(MainFragment.KEY_QUERY);
        }
        fragmentManager
                .beginTransaction()
                .replace(R.id.fcv, MainFragment.class, bundle)
                .commit();
    }
}