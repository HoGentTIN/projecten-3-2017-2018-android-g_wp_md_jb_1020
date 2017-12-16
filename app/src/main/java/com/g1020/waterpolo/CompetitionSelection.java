package com.g1020.waterpolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import Domain.Player;
import application.ApplicationRuntime;

import Domain.Division;
import Domain.Domaincontroller;
import okhttp3.ResponseBody;
import persistency.DivisionRest;
import persistency.MatchRest;
import persistency.PlayerRest;
import rest.ApiClient;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class CompetitionSelection extends AppCompatActivity implements MatchFragment.OnMatchSelectedListener,MatchSettingsFragment.onTeamclickedinteractionListener,MatchSettingsFragment.onArrowclickedinteractionListener
,PlayersMatchSettingsFragment.onPlayerClickedInteractionListener{

    private static final String TAG = CompetitionSelection.class.getSimpleName();

    ApplicationRuntime ar;  //this adds temporary code to this class
    Domaincontroller dc;
    MatchFragment matches;
    MatchSettingsFragment matchSettings;
    PlayersMatchSettingsFragment playersFrag;
//save the position of the selectedmatch
    private int position;

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



    apiService = dc.getApiService();
    Call<List<DivisionRest>> call2 = apiService.getDivisions();
        call2.enqueue(new Callback<List<DivisionRest>>() {
        @Override
        public void onResponse(Call<List<DivisionRest>> call, Response<List<DivisionRest>> response) {
            List<DivisionRest> divisionsR = response.body();
            dc.setDivisions(divisionsR);




            Log.d(TAG,"Retrieved " + divisionsR.size() + " division objects.");

            //for each match offical has filter matches

            //retrieve for each match the teams and needed info



        }

        @Override
        public void onFailure(Call<List<DivisionRest>> call, Throwable t) {
            Log.e(TAG,t.toString());
        }
    });

    //show list of match ..... vs ......


}

    //TEMP function to move to administration activity
    public void endSelection(View view) {

        //checking if there are 13 starters in each team
        if (this.checkTeams()) {

            dc.convertBackendToClass();
            //een keer de update van hun starters doen voor de hometeam
            //aantal spelers tellen om de array een grootte te geven
            int aantal = 0;
            aantal += dc.getSelectedMatch().getHome().getPlayers().size();
            aantal += dc.getSelectedMatch().getVisitor().getPlayers().size();

            List<ApiClient.Starter> upstarters = new ArrayList<>();
            int teller = 0;
            List<PlayerRest> players = dc.getSelectedMatch().getHome().getPlayers();
            for (PlayerRest player : players) {
                int starter;
                if (player.getStarter())
                    starter = 1;
                else
                    starter = 0;
                upstarters.add(new ApiClient.Starter(player.getPlayerId(), starter));
                teller += 1;

            }
            //en nu eens voor de visitor
            players = dc.getSelectedMatch().getHome().getPlayers();
            for (PlayerRest player : players) {
                int starter;
                if (player.getStarter())
                    starter = 1;
                else
                    starter = 0;
                upstarters.add(new ApiClient.Starter(player.getPlayerId(), starter));
                teller += 1;
            }


            //apiService.updateStarter(dc.getSelectedMatch().getMatch_id(),upstarters);
            ApiClient.ArrayListStarters arrStarters = new ApiClient.ArrayListStarters();
            arrStarters.addStarters((ArrayList<ApiClient.Starter>) upstarters);

            dc.asyncUpdateStarters(arrStarters);


            Intent intent = new Intent(this, MatchControl.class);
            //Intent intent = new Intent(this, AdministrationSetup.class);
            startActivity(intent);
        }
    }

    @Override
    public void onMatchSelected(int position) {
this.position = position;
        matchSettings = new MatchSettingsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.matchSettingsContainer,matchSettings).commit();


        playersFrag = new PlayersMatchSettingsFragment();
        playersFrag.setHometeam(1);//keeps adding to list on reloading the fragment try to prevent this TO DO
        getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer1,playersFrag).commit();


        hometeam =1;


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



        logoFrag = new LogoFragment();
        logoFrag.setHometeam(id);
        getSupportFragmentManager().beginTransaction().replace(R.id.logoContainer,logoFrag).commit();

    }

    @Override
    public void switchPlayer(boolean starter) {
        //if starter = true then that means he comes from the column from the screen  where starter = true(the left column) and vice versa
        if(selectedPlayer!=null) {
            if (starter) {
//these ifs will block the possibility of removing more than one active player from the list just so you know what number the player you will add afterwards will get
                if (hometeam == 1) {
                    int counter = 0;


                    for (PlayerRest player : dc.getSelectedMatch().getHome().getPlayers()) {

                        if (player.getStarter()) {
                            counter += 1;

                        }

                    }
                    if (counter == 13) {

                        this.selectedPlayer.setStarter(0);
                    }


                }

             else if (hometeam != 1) {
                int counter = 0;

                for (PlayerRest player : dc.getSelectedMatch().getVisitor().getPlayers()) {
                    if (player.getStarter()) {
                        counter += 1;

                    }
                }
                if (counter == 13) {

                    this.selectedPlayer.setStarter(0);
                }

            }


        }
        //here is the else statement when a player wants to become an active player
        else
        {
            if(hometeam==1){
            int counter =0;
            List<Integer> playernumbers=  new ArrayList<>();

            //here I normally check if there are already 13 active players
            for(PlayerRest player : dc.getSelectedMatch().getHome().getPlayers()){

                if( player.getStarter()) {
                    counter += 1;
                    playernumbers.add(player.getPlayerNumber());
                }

            }
            if(counter<13) {
                for(int i=1;i<=13;i++) {
                    if(!playernumbers.contains(i)){
                        this.selectedPlayer.setPlayerNumber(i);
                    this.selectedPlayer.setStarter(1);
                        dc.asyncUpdatePlayerNumber(selectedPlayer.getPlayerId(),i);}
                }

            }

        }
        else if(hometeam!=1){
            int counter =0;
                List<Integer> playernumbers=  new ArrayList<>();
            for(PlayerRest player : dc.getSelectedMatch().getVisitor().getPlayers()){
                if( player.getStarter()) {
                    counter += 1;
                    playernumbers.add(player.getPlayerNumber());
                }
            }
                if(counter<13) {
                    for(int i=1;i<=13;i++) {
                        if(!playernumbers.contains(i)){
                            this.selectedPlayer.setPlayerNumber(i);
                            this.selectedPlayer.setStarter(1);
                            apiService.updateNumber(selectedPlayer.getPlayerId(),i);
                        }
                    }

                }
        }

        }

        playersFrag = new PlayersMatchSettingsFragment();
        playersFrag.setHometeam(hometeam);
        getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer1,playersFrag).commit();}

    }

    @Override
    public void setSelectedPlayer(PlayerRest selectedPlayer) {
        this.selectedPlayer = selectedPlayer;
    }
    public void cancelMatch(View view){
        dc.asyncCancelMatch();
        finish();
        startActivity(getIntent());
    }
    public void changeMatch(){
        matches.changeMatch(this.position);
    }

    // custom class to use for the api put of updatestarters

    private boolean checkTeams(){
        List<PlayerRest> homePlayers = new ArrayList<PlayerRest>();
        List<PlayerRest> visitorPlayers = new ArrayList<PlayerRest>();

        for(PlayerRest p : dc.getSelectedMatch().getHome().getPlayers()){
            if(p.getStarter()){
                homePlayers.add(p);
            }
        }
        for(PlayerRest p : dc.getSelectedMatch().getVisitor().getPlayers()){
            if(p.getStarter()){
                visitorPlayers.add(p);
            }
        }

        if(homePlayers.size()==13 && visitorPlayers.size()==13)
            return true;

            return false;

    }

}
