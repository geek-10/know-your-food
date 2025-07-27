package com.example.knowyourfood;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoader extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageview;
    public ImageLoader(ImageView imageView){
        this.imageview = imageView;
    }
    protected Bitmap doInBackground(String... urls) {
        String imageUrl = urls[0];
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            // Update your ImageView with the downloaded image
            imageview.setImageBitmap(result);
        } else {
            // Handle error if bitmap is null
        }
    }
}