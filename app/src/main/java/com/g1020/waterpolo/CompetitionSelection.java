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

    //techicaly a boolean but it is an int now because the backend also works with ints
    private int hometeam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_selection);

        //Get runtime singleton class
        ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();



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


            }

            @Override
            public void onFailure(Call<List<MatchRest>> call, Throwable t) {
                Log.e(TAG,t.toString());
            }
        });




//same thing for the divisions as for the matches
    apiService = dc.getApiService();
    Call<List<DivisionRest>> call2 = apiService.getDivisions();
        call2.enqueue(new Callback<List<DivisionRest>>() {
        @Override
        public void onResponse(Call<List<DivisionRest>> call, Response<List<DivisionRest>> response) {
            List<DivisionRest> divisionsR = response.body();
            DivisionRest nullDivision =  new DivisionRest();
            nullDivision.setDivision_name("All");
            divisionsR.add(0,nullDivision);
            dc.setDivisions(divisionsR);




            Log.d(TAG,"Retrieved " + divisionsR.size() + " division objects.");





        }

        @Override
        public void onFailure(Call<List<DivisionRest>> call, Throwable t) {
            Log.e(TAG,t.toString());
        }
    });




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


            //this is a list of a custom object defined in apiclient to send a complete list op players that should be updated
            // the update is to set the starter variable that there will be 13 starters
            List<ApiClient.Starter> upstarters = new ArrayList<>();

            List<PlayerRest> players = dc.getSelectedMatch().getHome().getPlayers();
            for (PlayerRest player : players) {
                int starter;
                if (player.getStarter())
                    starter = 1;
                else
                    starter = 0;
                upstarters.add(new ApiClient.Starter(player.getPlayerId(), starter));


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

            }

            ApiClient.ArrayListStarters arrStarters = new ApiClient.ArrayListStarters();
            arrStarters.addStarters((ArrayList<ApiClient.Starter>) upstarters);

            dc.asyncUpdateStarters(arrStarters);


            Intent intent = new Intent(this, MatchControl.class);

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
//we zetten alles op voor de hometeam aangezien we kunnen wisselen binnen match tussen de twee ploegen en we met hometeam als standaard beginne

        hometeam =1;

        logoFrag = new LogoFragment();
        logoFrag.setHometeam(1);
        getSupportFragmentManager().beginTransaction().replace(R.id.logoContainer,logoFrag).commit();

    }

    @Override
    public void onFiltered() {

        // this is to throw away the fragment of the opened match if it has been opened
        if(matchSettings != null){
        MatchSettingsFragment fragment = (MatchSettingsFragment)getSupportFragmentManager().findFragmentById(matchSettings.getId());
        if(fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();}
    }

    @Override
    public void changeTeams(int id) {
        //This is to show antother team of the two teams in the match
        hometeam = id;
        playersFrag = new PlayersMatchSettingsFragment();
        playersFrag.setHometeam(id);

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
        dc.cancelMatch();
        finish();
        startActivity(getIntent());
    }
    public void changeMatch(){
        matches.changeMatch(this.position);
    }

    // custom class to use for the api put of updatestarters

    private boolean checkTeams(){

        // a check if that both teams have 13 starters
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
