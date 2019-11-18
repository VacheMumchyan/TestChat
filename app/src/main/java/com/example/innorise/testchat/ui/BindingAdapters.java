package com.example.innorise.testchat.ui;

import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class BindingAdapters {

    @BindingAdapter("app:setImage")
    public static void loadImage(ImageView imageView, Bitmap bitmap){
        Glide.with(imageView.getContext()).load(bitmap).into(imageView);
    }
}
