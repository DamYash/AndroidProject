package com.example.androidproject;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;
import static com.example.androidproject.FavoriteContract.TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import java.util.ArrayList;

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

    private GDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FavoriteContract.QUERY_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean delete(String id) {
        return getWritableDatabase().delete(
                TABLE_NAME, FavoriteContract.COLUMN_ID + "=?", new String[]{id}
        ) > 0;
    }

    public ArrayList<ArticleModel> get() {
        ArrayList<ArticleModel> models = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(
                TABLE_NAME, null, null, null, null, null, null
        );
        if (cursor != null) {
            if (cursor.moveToPosition(0)) {
                do {
                    ArticleModel articleModel = new ArticleModel(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3)
                    );
                    models.add(articleModel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return models;
    }

    public boolean save(@NonNull ArticleModel model) {
        ContentValues values = new ContentValues();
        values.put(FavoriteContract.COLUMN_ID, model.getId());
        values.put(FavoriteContract.COLUMN_TITLE, model.getTitle());
        values.put(FavoriteContract.COLUMN_URL, model.getUrl());
        values.put(FavoriteContract.COLUMN_SECTION_NAME, model.getSectionName());
        long result = getWritableDatabase()
                .insertWithOnConflict(TABLE_NAME, null, values, CONFLICT_REPLACE);
        return result > 0;
    }
}
