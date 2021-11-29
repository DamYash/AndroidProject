package com.example.androidproject;

import android.os.Parcel;
import android.os.Parcelable;

public class ArticleModel implements Parcelable {
    private String id;
    private String title;
    private String url;
    private String sectionName;

    public ArticleModel(String id, String title, String url, String sectionName) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.sectionName = sectionName;
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

    public static Creator<ArticleModel> getCREATOR() {
        return CREATOR;
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
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.title = source.readString();
        this.url = source.readString();
        this.sectionName = source.readString();
    }

    protected ArticleModel(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.url = in.readString();
        this.sectionName = in.readString();
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
