package com.example.finalproject;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class ImageHandler {

    public byte[] getByteArray(ImageView imageView) {
        Bitmap img = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        return getBitmapAsByteArray(img);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
