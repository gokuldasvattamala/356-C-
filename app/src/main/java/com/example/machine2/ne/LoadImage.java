package com.example.machine2.ne;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;



/**
 * Created by machine4 on 4/2/16.
 */
public class LoadImage extends AsyncTask<String,Void,Bitmap> {
    ImageView imageView;

    public LoadImage(ImageView imageView){
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String...urls){
        String urlOfImage = urls[0];
        Bitmap logo = null;
        try{
            InputStream is = new URL(urlOfImage).openStream();
            logo = BitmapFactory.decodeStream(is);
        }catch(Exception e){
            e.printStackTrace();
        }
        return logo;
    }
    protected void onPostExecute(Bitmap result){
        imageView.setImageBitmap(result);
    }
}