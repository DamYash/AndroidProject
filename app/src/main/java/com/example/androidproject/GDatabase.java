package com.example.androidproject;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;
import static com.example.androidproject.GuardianContract.COLUMN_ID;
import static com.example.androidproject.GuardianContract.COLUMN_INDEX_ID;
import static com.example.androidproject.GuardianContract.COLUMN_INDEX_IS_FAVORITE;
import static com.example.androidproject.GuardianContract.COLUMN_INDEX_QUERY;
import static com.example.androidproject.GuardianContract.COLUMN_INDEX_SECTION_NAME;
import static com.example.androidproject.GuardianContract.COLUMN_INDEX_TITLE;
import static com.example.androidproject.GuardianContract.COLUMN_INDEX_URL;
import static com.example.androidproject.GuardianContract.COLUMN_IS_FAVORITE;
import static com.example.androidproject.GuardianContract.COLUMN_QUERY;
import static com.example.androidproject.GuardianContract.TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

public class GDatabase extends SQLiteOpenHelper {

    private static GDatabase guardianDatabase = null;

    @NonNull
    public static GDatabase getInstance(Context context) {
        if (guardianDatabase == null) {
            guardianDatabase = new GDatabase(context);
        }
        return guardianDatabase;
    }


    private static final String DATABASE_NAME = "guardian";

    private GDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(GuardianContract.QUERY_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean delete(String id) {
        return getWritableDatabase().delete(
                TABLE_NAME, COLUMN_ID + "=?", new String[]{id}
        ) > 0;
    }

    public List<ArticleModel> getData(Cursor cursor) {
        ArrayList<ArticleModel> models = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToPosition(0)) {
                do {
                    ArticleModel articleModel = new ArticleModel(
                            cursor.getString(COLUMN_INDEX_ID),
                            cursor.getString(COLUMN_INDEX_TITLE),
                            cursor.getString(COLUMN_INDEX_URL),
                            cursor.getString(COLUMN_INDEX_SECTION_NAME),
                            cursor.getString(COLUMN_INDEX_QUERY),
                            cursor.getInt(COLUMN_INDEX_IS_FAVORITE) > 0
                    );
                    models.add(articleModel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return models;
    }

    public List<ArticleModel> getFavorite() {
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_IS_FAVORITE + "=1 AND " + COLUMN_QUERY + "='favorite'", null
        );
        return getData(cursor);
    }

    public List<ArticleModel> get(String query) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + GuardianContract.COLUMN_QUERY + " LIKE '" + query + "'";
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        return getData(cursor);
    }

    public boolean saveToFavorite(@NonNull ArticleModel model) {
        model.setQuery("favorite");
        model.setFavorite(true);
        ContentValues values = new ContentValues();
        values.put(GuardianContract.COLUMN_ID, model.getId() + "1");
        values.put(GuardianContract.COLUMN_TITLE, model.getTitle());
        values.put(GuardianContract.COLUMN_URL, model.getUrl());
        values.put(GuardianContract.COLUMN_SECTION_NAME, model.getSectionName());
        values.put(GuardianContract.COLUMN_QUERY, model.getQuery());
        values.put(GuardianContract.COLUMN_IS_FAVORITE, model.isFavorite());
        long result = getWritableDatabase()
                .insertWithOnConflict(TABLE_NAME, null, values, CONFLICT_REPLACE);
        return result > 0;
    }

    public boolean save(@NonNull ArticleModel model) {
        ContentValues values = new ContentValues();
        values.put(GuardianContract.COLUMN_ID, model.getId());
        values.put(GuardianContract.COLUMN_TITLE, model.getTitle());
        values.put(GuardianContract.COLUMN_URL, model.getUrl());
        values.put(GuardianContract.COLUMN_SECTION_NAME, model.getSectionName());
        values.put(GuardianContract.COLUMN_QUERY, model.getQuery());
        values.put(GuardianContract.COLUMN_IS_FAVORITE, model.isFavorite());
        long result = getWritableDatabase()
                .insertWithOnConflict(TABLE_NAME, null, values, CONFLICT_REPLACE);
        return result > 0;
    }

    public int deleteWithQuery(String query) {
        return getWritableDatabase().delete(
                TABLE_NAME, COLUMN_QUERY + "=?", new String[]{query}
        );
    }

    public boolean isFavorite(String id) {
        String idFav = id + 1;
        boolean result = false;
        Cursor cursor = getWritableDatabase()
                .rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + "='" + idFav + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = cursor.getInt(COLUMN_INDEX_IS_FAVORITE) > 0;
            }
            cursor.close();
        }
        return result;
    }
}
