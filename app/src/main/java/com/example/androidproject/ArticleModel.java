package com.example.androidproject;

import android.os.Parcel;
import android.os.Parcelable;


public class ArticleModel implements Parcelable {
    private String id;
    private String title;
    private String url;
    private String sectionName;
    private String query;
    private boolean isFavorite;

    public ArticleModel(String id, String title, String url, String sectionName, String query, boolean isFavorite) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.sectionName = sectionName;
        this.query = query;
        this.isFavorite = isFavorite;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.sectionName);
        dest.writeString(this.query);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.title = source.readString();
        this.url = source.readString();
        this.sectionName = source.readString();
        this.query = source.readString();
        this.isFavorite = source.readByte() != 0;
    }

    protected ArticleModel(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.url = in.readString();
        this.sectionName = in.readString();
        this.query = in.readString();
        this.isFavorite = in.readByte() != 0;
    }

    public static final Creator<ArticleModel> CREATOR = new Creator<ArticleModel>() {
        @Override
        public ArticleModel createFromParcel(Parcel source) {
            return new ArticleModel(source);
        }

        @Override
        public ArticleModel[] newArray(int size) {
            return new ArticleModel[size];
        }
    };
}