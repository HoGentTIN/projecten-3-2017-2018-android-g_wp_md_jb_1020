package com.g1020.waterpolo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Application.ApplicationRuntime;
import Domain.Domaincontroller;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamsHeaderFragment extends Fragment {

    private TextView txtVHomeTeamName;
    private TextView txtVAwayTeamName;
    private TextView txtVHomeScore;
    private TextView txtVAwayScore;
    private TextView txtVPeriod;

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

        txtVHomeTeamName.setText(dc.getMatch().getTeam(0).getTeamName());
        txtVAwayTeamName.setText(dc.getMatch().getTeam(1).getTeamName());

       // Log.i("game", "Current period: " + Integer.toString(dc.getMatch().getQuarters().get(0).getQuarterPeriod()));
        txtVPeriod.setText(String.valueOf(dc.getMatch().getQuarters().get(0).getQuarterPeriod()));
        txtVHomeScore.setText(String.valueOf(dc.getMatch().getScoreHome()));
        txtVAwayScore.setText(String.valueOf(dc.getMatch().getScoreAway()));

        return view;
    }

    public void updateHeader(){
        txtVPeriod.setText(String.valueOf(dc.getMatch().getQuarters().get(0).getQuarterPeriod()));
        txtVHomeScore.setText(String.valueOf(dc.getMatch().getScoreHome()));
        txtVAwayScore.setText(String.valueOf(dc.getMatch().getScoreAway()));
    }

}
