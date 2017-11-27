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

import application.ApplicationRuntime;
import Domain.Domaincontroller;
import views.DownloadImageTask;


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
}