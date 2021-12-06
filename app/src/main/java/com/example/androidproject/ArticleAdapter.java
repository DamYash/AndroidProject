package com.example.androidproject;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class ArticleAdapter extends ArrayAdapter<ArticleModel>
{
    private final ItemClick itemClick;

    public interface ItemClick {
        void onClick(View v, ArticleModel model);
    }

    public ArticleAdapter(@NonNull Context context, ArrayList<ArticleModel> data, ItemClick item) {
        super(context, 0, data);
        this.itemClick = item;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ArticleModel data = getItem(position);
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_article, parent, false);
        view.findViewById(R.id.root).setOnClickListener(v -> itemClick.onClick(v, data));
        ((TextView) view.findViewById(R.id.web_title)).setText(data.getTitle());
        ((TextView) view.findViewById(R.id.web_url)).setText(data.getUrl());
        ((TextView) view.findViewById(R.id.section_name)).setText(data.getSectionName());
        return view;
    }
}