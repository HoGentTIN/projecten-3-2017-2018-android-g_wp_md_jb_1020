package com.g1020.waterpolo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

import Application.ApplicationRuntime;
import Domain.CompetitionClass;
import Domain.Domaincontroller;
import Domain.Team;
import persistency.MatchRest;
import rest.ApiClient;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompetitionSelection extends AppCompatActivity implements MatchFragment.OnMatchSelectedListener{

    private static final String TAG = CompetitionSelection.class.getSimpleName();

    ApplicationRuntime ar;  //this adds temporary code to this class
    Domaincontroller dc;
    MatchFragment matches;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_selection);


        //Get runtime singleton class
        ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();
        ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();

        // PIETER
        dc.startMatch();
        dc.createTeams("Oostende", CompetitionClass.U20,"Aalst",CompetitionClass.U20);
        dc.createPlayers();

        matches = new MatchFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.matchesContainer, matches).commit();

        //Retrieve list of Matches being played -- TEMPORARY THIS RETURNS ALL MATCHES PRACTICLY SHOULD ONLY BE MATCHES OF LOGGED IN OFFICIAL
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<MatchRest>> call = apiService.getMatches();
        call.enqueue(new Callback<List<MatchRest>>() {
            @Override
            public void onResponse(Call<List<MatchRest>> call, Response<List<MatchRest>> response) {
                List<MatchRest> matches = response.body();
                Log.d(TAG,"Retrieved " + matches.size() + " Match objects.");

                //for each match offical has filter matches

                //retrieve for each match the teams and needed info

                //When match selected show info in screen and list of players

                //use match to get the id's of both teams
                //call to retrieve team using that id, for both home and visitor


                //team should have list of players
                MatchRest mtest = matches.get(0);
                Log.d(TAG,"test playerfound " + mtest.getHome_id() );

            }

            @Override
            public void onFailure(Call<List<MatchRest>> call, Throwable t) {
                Log.e(TAG,t.toString());
            }
        });

        //show list of match ..... vs ......


    }

    //TEMP function to move to administration activity
    public void endSelection(View view) {
        //get selected match value from layout -> create Match object from the MatchRest Object and store it in the applicationruntime domaincontroller

        Intent intent = new Intent(this, MatchControl.class);
        //Intent intent = new Intent(this, AdministrationSetup.class);
        startActivity(intent);
    }

    @Override
    public void onMatchSelected(int matchNumber) {
        dc.setMatch(matchNumber);
    }
}
