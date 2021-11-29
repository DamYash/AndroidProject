package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

public class utils {

    public static void openBrowser(View view, String url) {
        Context context = view.getContext();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_browser)));
    }
}
