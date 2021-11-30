package com.example.androidproject;

import androidx.annotation.Nullable;

public class HttpError extends Throwable
{

    public HttpError(@Nullable String message)
    {
        super(message);
    }
}
