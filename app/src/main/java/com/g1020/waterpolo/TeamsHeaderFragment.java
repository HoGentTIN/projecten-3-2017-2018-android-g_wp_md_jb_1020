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

import application.ApplicationRuntime;
import Domain.Domaincontroller;
import views.DownloadImageTask;

/**
 * A simple {@link Fragment} subclass.
 * This fragment is used to display the team information used in {@link MatchControl} & {@link AdministrationEnd}
 */
public class TeamsHeaderFragment extends Fragment {

    private TextView txtVHomeScore;
    private TextView txtVAwayScore;
    private TextView txtVPeriod;

    private Domaincontroller dc;

    public TeamsHeaderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_teams_header, container, false);

        ApplicationRuntime ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();

        TextView txtVHomeTeamName = (TextView) view.findViewById(R.id.txtnamehometeam);
        TextView txtVAwayTeamName = (TextView) view.findViewById(R.id.txtnameawayteam);
        txtVPeriod = (TextView) view.findViewById(R.id.txtperiod);
        txtVHomeScore = (TextView) view.findViewById(R.id.txtscorehometeam);
        txtVAwayScore = (TextView) view.findViewById(R.id.txtscoreawayteam);

        ImageView imgHomeLogo = (ImageView) view.findViewById(R.id.imglogohometeam);
        ImageView imgAwayLogo = (ImageView) view.findViewById(R.id.imglogoawayteam);

        txtVHomeTeamName.setText(dc.getHomeTeam().getTeamName());
        txtVAwayTeamName.setText(dc.getAwayTeam().getTeamName());

        txtVPeriod.setText(String.valueOf(dc.getMatch().getCurrentRound()));
        txtVHomeScore.setText(String.valueOf(dc.getMatch().getScoreForTeam(dc.getHomeTeam().getTeam_id())));
        txtVAwayScore.setText(String.valueOf(dc.getMatch().getScoreForTeam(dc.getAwayTeam().getTeam_id())));

        new DownloadImageTask(imgHomeLogo).execute(dc.getHomeTeam().getLogo());
        new DownloadImageTask(imgAwayLogo).execute(dc.getAwayTeam().getLogo());

        return view;
    }

    /**
     * Method to update the periodnumber and team scores.
     */
    public void updateHeader(){
        txtVPeriod.setText(String.valueOf(dc.getMatch().getCurrentRound()));
        txtVHomeScore.setText(String.valueOf(dc.getMatch().getScoreForTeam(dc.getHomeTeam().getTeam_id())));
        txtVAwayScore.setText(String.valueOf(dc.getMatch().getScoreForTeam(dc.getAwayTeam().getTeam_id())));
    }
}
