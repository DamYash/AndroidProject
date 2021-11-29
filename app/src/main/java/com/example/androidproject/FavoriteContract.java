package com.example.androidproject;

public class FavoriteContract {

    public static final String QUERY_CREATE_TABLE = "CREATE TABLE " + FavoriteContract.TABLE_NAME + "(" + FavoriteContract.COLUMN_ID + " TEXT PRIMARY KEY, " + FavoriteContract.COLUMN_TITLE + " TEXT, " + FavoriteContract.COLUMN_URL + " TEXT, " + FavoriteContract.COLUMN_SECTION_NAME + " TEXT)";

    public static final String TABLE_NAME = "favorite_article";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_SECTION_NAME = "section_name";
}
