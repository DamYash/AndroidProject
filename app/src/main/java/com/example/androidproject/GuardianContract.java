package com.example.androidproject;

public class GuardianContract {

    public static final String QUERY_CREATE_TABLE = "CREATE TABLE " + GuardianContract.TABLE_NAME + "(" + GuardianContract.COLUMN_ID + " TEXT PRIMARY KEY, " + GuardianContract.COLUMN_TITLE + " TEXT, " + GuardianContract.COLUMN_URL + " TEXT, " + GuardianContract.COLUMN_SECTION_NAME + " TEXT, " + GuardianContract.COLUMN_QUERY + " TEXT, " + GuardianContract.COLUMN_IS_FAVORITE + " INTEGER)";

    public static final String TABLE_NAME = "article";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_SECTION_NAME = "section_name";
    public static final String COLUMN_QUERY = "query_";
    public static final String COLUMN_IS_FAVORITE = "is_favorite";

    public static final int COLUMN_INDEX_ID = 0;
    public static final int COLUMN_INDEX_TITLE = 1;
    public static final int COLUMN_INDEX_URL = 2;
    public static final int COLUMN_INDEX_SECTION_NAME = 3;
    public static final int COLUMN_INDEX_QUERY = 4;
    public static final int COLUMN_INDEX_IS_FAVORITE = 5;
}
