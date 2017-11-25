package com.g1020.waterpolo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import Application.ApplicationRuntime;
import Domain.Domaincontroller;


public class LogoFragment extends Fragment {
    private ApplicationRuntime ar;  //this adds temporary code to this class
    private Domaincontroller dc;

    private boolean hometeam;

    ImageView imglogo;


    public LogoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_logo, container, false);
        ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();
        URL url = null;
        imglogo = (ImageView) view.findViewById(R.id.imgLogo);
        if (hometeam) {
            new DownloadImageTask(imglogo).execute(dc.getSelectedMatch().getHome().getLogo());
        } else {
            new DownloadImageTask(imglogo).execute(dc.getSelectedMatch().getVisitor().getLogo());
        }


        return view;

    }

    public void setHometeam(int i) {
        if (i == 1)
            this.hometeam = true;
        else
            this.hometeam = false;
    }


    // als dit zou werken is dit een apart klasse om op een juiste manier de afbeelding te kunnen inladen
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}