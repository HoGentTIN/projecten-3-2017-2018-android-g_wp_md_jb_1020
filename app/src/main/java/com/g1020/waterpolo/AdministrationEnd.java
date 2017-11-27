package com.g1020.waterpolo;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;

import application.ApplicationRuntime;
import Domain.Domaincontroller;

public class AdministrationEnd extends AppCompatActivity implements PlayersFragment.OnPlayerSelectedListener {

    ButtonsFragment btnFragment;
    TeamsHeaderFragment teamsHeaderFragment;
    PlayersFragment homeTeam;
    PlayersFragment awayTeam;

    ListView lvActivitiesQ1,lvActivitiesQ2, lvActivitiesQ3, lvActivitiesQ4;
    List q1,q2,q3,q4;

    TextInputLayout txtInputLayName, txtInputLayCode;

    ApplicationRuntime ar;  //this adds temporary code to this class
    Domaincontroller dc;
    MatchControl mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administration_end);

        ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();
        mc = (MatchControl) dc.getCurrentActivity();

        txtInputLayName = (TextInputLayout) findViewById(R.id.txtInputLayName);
        txtInputLayCode = (TextInputLayout) findViewById(R.id.txtInputLayCode);

        btnFragment = new ButtonsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.adminbuttonsContainer, btnFragment).commit();

        teamsHeaderFragment = new TeamsHeaderFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.teamsheadercontainer, teamsHeaderFragment).commit();

        homeTeam = PlayersFragment.newInstance(0);
        getSupportFragmentManager().beginTransaction().add(R.id.homeContainer, homeTeam).commit();

        awayTeam = PlayersFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction().add(R.id.awayContainer, awayTeam).commit();

        TabHost host = (TabHost)findViewById(R.id.tab_host);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("Quarter 1")
                .setContent(R.id.activitiesContainerFirstRound)
                .setIndicator("Quarter 1");

        host.addTab(spec);

        spec = host.newTabSpec("Quarter 2");
        spec.setContent(R.id.activitiesContainerSecondRound);
        spec.setIndicator("Quarter 2");
        host.addTab(spec);

        spec = host.newTabSpec("Quarter 3");
        spec.setContent(R.id.activitiesContainerThirdRound);
        spec.setIndicator("Quarter 3");
        host.addTab(spec);

        spec = host.newTabSpec("Quarter 4");
        spec.setContent(R.id.activitiesContainerFourthRound);
        spec.setIndicator("Quarter 4");
        host.addTab(spec);

        lvActivitiesQ1 = (ListView) findViewById(R.id.activitiesContainerFirstRound) ;
        q1 = dc.getLogForRound(1);
        lvActivitiesQ1.setAdapter(new ArrayAdapter<String>(AdministrationEnd.this,
                android.R.layout.simple_list_item_1, q1));

        lvActivitiesQ2 = (ListView) findViewById(R.id.activitiesContainerSecondRound) ;
        q2 = dc.getLogForRound(2);
        lvActivitiesQ2.setAdapter(new ArrayAdapter<String>(AdministrationEnd.this,
                android.R.layout.simple_list_item_1, q2));

        lvActivitiesQ3 = (ListView) findViewById(R.id.activitiesContainerThirdRound) ;
        q3 = dc.getLogForRound(3);
        lvActivitiesQ3.setAdapter(new ArrayAdapter<String>(AdministrationEnd.this,
                android.R.layout.simple_list_item_1, q3));

        lvActivitiesQ4 = (ListView) findViewById(R.id.activitiesContainerFourthRound) ;
        q4 = dc.getLogForRound(4);
        lvActivitiesQ4.setAdapter(new ArrayAdapter<String>(AdministrationEnd.this,
                android.R.layout.simple_list_item_1, q4));



        //setPasswordVisibilityToggleEnabled(boolean)

    }


    public void SignMatch(View view) {
    }

    @Override
    public void onArticleSelected(Boolean homeTeam, int playerId) {
        dc.setSelectedPlayer(homeTeam, playerId);
    }
}
