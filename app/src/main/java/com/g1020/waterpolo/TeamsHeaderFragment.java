package com.g1020.waterpolo;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import Application.ApplicationRuntime;
import Domain.Domaincontroller;
import views.DownloadImageTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamsHeaderFragment extends Fragment {

    private TextView txtVHomeTeamName;
    private TextView txtVAwayTeamName;
    private TextView txtVHomeScore;
    private TextView txtVAwayScore;
    private TextView txtVPeriod;

    private ImageView imgHomeLogo, imgAwayLogo;

    private Drawable d;

    private ApplicationRuntime ar;  //this adds temporary code to this class
    private Domaincontroller dc;

    public TeamsHeaderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_teams_header, container, false);

        ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();

        txtVHomeTeamName = (TextView) view.findViewById(R.id.txtnamehometeam);
        txtVAwayTeamName = (TextView) view.findViewById(R.id.txtnameawayteam);
        txtVPeriod = (TextView) view.findViewById(R.id.txtperiod);
        txtVHomeScore = (TextView) view.findViewById(R.id.txtscorehometeam);
        txtVAwayScore = (TextView) view.findViewById(R.id.txtscoreawayteam);

        imgHomeLogo = (ImageView) view.findViewById(R.id.imglogohometeam);
        imgAwayLogo = (ImageView) view.findViewById(R.id.imglogoawayteam);

        txtVHomeTeamName.setText(dc.getHomeTeam().getTeamName());
        txtVAwayTeamName.setText(dc.getAwayTeam().getTeamName());

       // Log.i("game", "Current period: " + Integer.toString(dc.getMatch().getQuarters().get(0).getQuarterPeriod()));
        txtVPeriod.setText(String.valueOf(dc.getMatch().getCurrentRound()));
        txtVHomeScore.setText(String.valueOf(dc.getMatch().getScoreForTeam(dc.getHomeTeam().getTeam_id())));
        txtVAwayScore.setText(String.valueOf(dc.getMatch().getScoreForTeam(dc.getAwayTeam().getTeam_id())));


        new DownloadImageTask(imgHomeLogo).execute(dc.getHomeTeam().getLogo());
        new DownloadImageTask(imgAwayLogo).execute(dc.getAwayTeam().getLogo());



    /*    try {
            imgHomeLogo.setImageBitmap(createScaledBitmap(loadTeamLogo(true),100));
            imgAwayLogo.setImageBitmap(createScaledBitmap(loadTeamLogo(false),100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    */
        return view;
    }

    public void updateHeader(){
        txtVPeriod.setText(String.valueOf(dc.getMatch().getCurrentRound()));
        txtVHomeScore.setText(String.valueOf(dc.getMatch().getScoreForTeam(dc.getHomeTeam().getTeam_id())));
        txtVAwayScore.setText(String.valueOf(dc.getMatch().getScoreForTeam(dc.getAwayTeam().getTeam_id())));
    }

    /*
    // returns initial bitmap from team logo url
    private Bitmap loadTeamLogo(final boolean flag) throws InterruptedException {

        final Bitmap[] bmp = {null};
        // new thread because network operation can't be on main thread
        Runnable task = new Runnable() {
            @Override
            public void run(){
                try {
                    URL urlLogo;

                    if(flag) {
                        urlLogo = new URL(dc.getHomeTeam().getLogo());
                        Log.i("game", urlLogo.toString());
                    } else {
                        urlLogo = new URL(dc.getAwayTeam().getLogo());
                    }
                    //create image from url
                    bmp[0] = BitmapFactory.decodeStream(urlLogo.openConnection().getInputStream());
                    Log.i("game", bmp[0].toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t = new Thread(task, "Logos");
        t.start();
        try {
            //wait till thread is finished so bitmap isn't null
            t.join();
            return bmp[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // scale bitmap to preferred size
    private Bitmap createScaledBitmap(Bitmap bmp, int size){
        if(bmp != null) {
            Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, size, size, false);
            return scaledBmp;
        }
        return null;
    }
    */
}
