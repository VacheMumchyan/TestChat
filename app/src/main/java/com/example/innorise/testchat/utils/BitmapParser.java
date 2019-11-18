package com.example.innorise.testchat.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BitmapParser {

    private static ByteArrayOutputStream outputStream;

    public static Bitmap urlToBitmap(String path) {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(path);
        }
        return BitmapFactory.decodeStream(fis);

    }

    public static Bitmap base64ToBitmap(String data) {
        byte[] b = Base64.decode(data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(b, 0, b.length);

    }

    public static String bitmapToBase64(Bitmap bitmap) {
        if (bitmap != null) {
            outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        }
        return null;


    }
}
