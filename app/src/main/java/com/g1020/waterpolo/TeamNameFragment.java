package com.g1020.waterpolo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import application.ApplicationRuntime;
import Domain.Domaincontroller;


public class TeamNameFragment extends Fragment {

    private ApplicationRuntime ar;  //this adds temporary code to this class
    private Domaincontroller dc;

    private boolean hometeam;

    TextView txtTeamName;



    public TeamNameFragment() {
        // Required empty public constructor
    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_team_name, container, false);
        ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();
        txtTeamName  =(TextView) view.findViewById(R.id.txtTeamName);
        if(hometeam)
            txtTeamName.setText(dc.getSelectedMatch().getHome().getTeamName());
        else
            txtTeamName.setText(dc.getSelectedMatch().getVisitor().getTeamName());
        return view;

    }
    public void setHometeam(int i){
        if(i==1)
            this.hometeam=true;
        else
            this.hometeam=false;
    }






}
