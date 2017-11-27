package com.g1020.waterpolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import Application.ApplicationRuntime;

import Domain.Division;
import Domain.Domaincontroller;
import persistency.MatchRest;
import persistency.PlayerRest;
import rest.ApiClient;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompetitionSelection extends AppCompatActivity implements MatchFragment.OnMatchSelectedListener,MatchSettingsFragment.onTeamclickedinteractionListener,MatchSettingsFragment.onArrowclickedinteractionListener
,PlayersMatchSettingsFragment.onPlayerClickedInteractionListener{

    private static final String TAG = CompetitionSelection.class.getSimpleName();

    ApplicationRuntime ar;  //this adds temporary code to this class
    Domaincontroller dc;
    MatchFragment matches;
    MatchSettingsFragment matchSettings;
    PlayersMatchSettingsFragment playersFrag;
    TeamNameFragment teamNameFrag;
    LogoFragment logoFrag;
    ApiInterface apiService;
    private PlayerRest selectedPlayer;
    private int hometeam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_selection);

        //Get runtime singleton class
        ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();

        //original testcode for hardcoded match
        //dc.startMatch();
        //dc.createTeams("Oostende", new Division("U20", 7,2),"Aalst",new Division("U20", 7,2));
        //dc.createPlayers();

        //matches = new MatchFragment();
        //getSupportFragmentManager().beginTransaction().add(R.id.matchesContainer, matches).commit();


        //Retrieve list of Matches being played -- TEMPORARY THIS RETURNS ALL MATCHES PRACTICLY SHOULD ONLY BE MATCHES OF LOGGED IN OFFICIAL
        apiService = dc.getApiService();
        Call<List<MatchRest>> call = apiService.getMatches();
        call.enqueue(new Callback<List<MatchRest>>() {
            @Override
            public void onResponse(Call<List<MatchRest>> call, Response<List<MatchRest>> response) {
                List<MatchRest> matchesR = response.body();
                dc.setOwnedMatchesR(matchesR);

                matches = new MatchFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.matchesContainer, matches).commit();


                Log.d(TAG,"Retrieved " + matchesR.size() + " Match objects.");

                //for each match offical has filter matches

                //retrieve for each match the teams and needed info
                MatchRest mtest = matchesR.get(0);
                String ptest = mtest.getHome().getPlayers().get(0).getName();
                //Log.d(TAG,"random test player retrival from Matchrest object: " + ptest + ". Player who made the first goal " + mtest.getGoals().get(0).getPlayer().getName());

                /* Technicly not needed since Match knows its TeamRest objects and its PlayerRestObjects - Normaly when a match is finished it cannot be played anymore so should always initialize on empty array
                Call<TeamRest> tCallHome = apiService.getTeam(mtest.getHome_id());
                Call<TeamRest> tCallVisitor = apiService.getTeam(mtest.getVisitor_id());

                tCallHome.enqueue(new Callback<TeamRest>() {
                    @Override
                    public void onResponse(Call<TeamRest> call, Response<TeamRest> response) {
                        TeamRest tHomeTest = response.body();
                        Log.d(TAG,"Retrieved home team " + tHomeTest.getTeamName());

                    }

                    @Override
                    public void onFailure(Call<TeamRest> call, Throwable t) {
                        Log.e(TAG,t.toString());
                    }
                });
                tCallVisitor.enqueue(new Callback<TeamRest>() {
                    @Override
                    public void onResponse(Call<TeamRest> call, Response<TeamRest> response) {
                        TeamRest tVisitorTest = response.body();
                        Log.d(TAG,"Retrieved visitor team " + tVisitorTest.getTeamName());
                        List<PlayerRest> playersVisitor = tVisitorTest.getPlayers();
                        for (PlayerRest p : playersVisitor){
                            Log.d(TAG,"Retrieved visitor players " + p.getName());
                        }
                    }

                    @Override
                    public void onFailure(Call<TeamRest> call, Throwable t) {
                        Log.e(TAG,t.toString());
                    }
                });
                */



                //When match selected show info in screen and list of players

                //use match to get the id's of both teams
                //call to retrieve team using that id, for both home and visitor


                //team should have list of players


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

        dc.convertBackendToClass();
        //een keer de update van hun starters doen voor de hometeam
        //aantal spelers tellen om de array een grootte te geven
        int aantal=0;
        aantal +=dc.getSelectedMatch().getHome().getPlayers().size();
        aantal+=dc.getSelectedMatch().getVisitor().getPlayers().size();

        List<ApiClient.Starter> upstarters = new ArrayList<>();
int teller =0;
        List<PlayerRest> players = dc.getSelectedMatch().getHome().getPlayers();
        for(PlayerRest player : players){
            int starter;
            if(player.getStarter())
                starter = 1;
            else
                starter = 2;
            upstarters.add(new ApiClient.Starter(player.getPlayerId(), starter));
            teller+=1;

        }
        //en nu eens voor de visitor
        players = dc.getSelectedMatch().getHome().getPlayers();
        for(PlayerRest player : players){
            int starter;
            if(player.getStarter())
                starter = 1;
            else
                starter = 2;
            upstarters.add(new ApiClient.Starter(player.getPlayerId(), starter));
            teller+=1;
        }


        //apiService.updateStarter(dc.getSelectedMatch().getMatch_id(),upstarters);
        ApiClient.ArrayListStarters arrStarters = new ApiClient.ArrayListStarters();
        arrStarters.addStarters((ArrayList<ApiClient.Starter>) upstarters);

       // apiService.putListOfStarters(dc.getSelectedMatch().getMatch_id(), arrStarters);


        Intent intent = new Intent(this, MatchControl.class);
        //Intent intent = new Intent(this, AdministrationSetup.class);
        startActivity(intent);
    }

    @Override
    public void onMatchSelected(int matchNumber) {
        dc.setMatch(matchNumber);
        matchSettings = new MatchSettingsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.matchSettingsContainer,matchSettings).commit();
        playersFrag = new PlayersMatchSettingsFragment();
        playersFrag.setHometeam(1);//keeps adding to list on reloading the fragment try to prevent this TO DO
        getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer1,playersFrag).commit();

        teamNameFrag = new TeamNameFragment();
        hometeam =1;
        teamNameFrag.setHometeam(1);
        getSupportFragmentManager().beginTransaction().replace(R.id.llTeamName,teamNameFrag).commit();
        logoFrag = new LogoFragment();
        logoFrag.setHometeam(1);
        getSupportFragmentManager().beginTransaction().replace(R.id.logoContainer,logoFrag).commit();



    }

    @Override
    public void changeTeams(int id) {
        hometeam = id;
        playersFrag = new PlayersMatchSettingsFragment();
        playersFrag.setHometeam(id);
        //keeps adding to list on reloading the fragment try to prevent this TO DO
        getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer1,playersFrag).commit();
        teamNameFrag = new TeamNameFragment();
        teamNameFrag.setHometeam(id);
        getSupportFragmentManager().beginTransaction().replace(R.id.llTeamName,teamNameFrag).commit();
        logoFrag = new LogoFragment();
        logoFrag.setHometeam(id);
        getSupportFragmentManager().beginTransaction().replace(R.id.logoContainer,logoFrag).commit();

    }

    @Override
    public void switchPlayer(boolean starter) {
        //if starter = true then that means he comes from the column where starter = true and vice versa
        if(selectedPlayer!=null){
        if(starter){
            this.selectedPlayer.setStarter(0);
        }
        else
        {
            if(hometeam==1){
              int counter =0;
              for(PlayerRest player : dc.getSelectedMatch().getHome().getPlayers()){
                  if( player.getStarter())
                      counter+=1;
              }
              if(counter<13)
                  this.selectedPlayer.setStarter(1);
            }
            else if(hometeam!=1){
                int counter =0;
                for(PlayerRest player : dc.getSelectedMatch().getVisitor().getPlayers()){
                    if( player.getStarter())
                        counter+=1;
                }
                if(counter<13)
                    this.selectedPlayer.setStarter(1);
            }
            this.selectedPlayer.setStarter(1);
        }
        playersFrag = new PlayersMatchSettingsFragment();
        playersFrag.setHometeam(hometeam);
        getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer1,playersFrag).commit();}

    }

    @Override
    public void setSelectedPlayer(PlayerRest selectedPlayer) {
        this.selectedPlayer = selectedPlayer;
    }

    // custom class to use for the api put of updatestarters

}
