package com.g1020.waterpolo;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.*;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import Application.ApplicationRuntime;
import Domain.CompetitionClass;
import Domain.Domaincontroller;
import Domain.Match;
import Domain.MatchTimer;
import Domain.Status;
import persistency.MatchContract;
import persistency.MatchRest;
import persistency.MatchRestResponse;
import rest.ApiClient;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchControl extends AppCompatActivity {

    private static final String TAG = MatchControl.class.getSimpleName();
    private final static String API_KEY = "";

    ApplicationRuntime ar;  //this adds temporary code to this class
    Domaincontroller dc;
    MatchTimer matchTimer;

    TeamsHeaderFragment teamsHeader;
    PlayersFragment homeTeam;
    PlayersFragment awayTeam;
    FaultFragment faultAwayTeam;
    FaultFragment faultHomeTeam;
    ActivityFragment activities;
    ActivityInfoFragment infoFragment;
    ActivityButtonsFragment btnFragment;

    //LIFECYCLE FUNCTIONS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_control);

        ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();

        // PIETER
        dc.startMatch();
        dc.createTeams("Oostende", CompetitionClass.U20,"Aalst",CompetitionClass.U20);
        dc.createPlayers();
        // END PIETER

        matchTimer = ar.chronoSetup((TextView) findViewById(R.id.txtTimer), dc.getRoundTime());
        /*
        //Chrono listner to set maximum time
        matchTimer.getMatchTimer().setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if(matchTimer.checkRoundTime(chronometer.getText().toString())){
                    Log.i("Info","Timer reached round end");
                    dc.appendLog("End of round" + 1 + " - Current matchscore: " + 2 + " - " + 5,"ME");                                              //Replace 1 by dc.functiontoretrieveroundnumber, and retrievecurrentscores home-away
                    //checkRoundTime already does the timerprocess
                    //Still need code here for actions related to activity
                }
            }
        });
        */
        //initialize shotlock timer
        matchTimer.initShotlock((TextView) findViewById(R.id.txtShotlock), (long) 30000);
        matchTimer.initTimer((TextView) findViewById(R.id.txtTimer), (long) (8*1000*60));

        teamsHeader = new TeamsHeaderFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.teamsheadercontainer, teamsHeader).commit();

        //homeTeam = new PlayersFragment();
        homeTeam = PlayersFragment.newInstance(0);
        getSupportFragmentManager().beginTransaction().add(R.id.homeContainer, homeTeam).commit();

        //awayTeam = new PlayersFragment();
        awayTeam = PlayersFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction().add(R.id.awayContainer, awayTeam).commit();

        dc.appendLog("Round 1 started","SR1","8:00",1); //relocate to startchrono for first time only here for testing
        activities = ActivityFragment.newInstance(1);

        getSupportFragmentManager().beginTransaction().add(R.id.activitiesContainer, activities).commit();

        btnFragment = new ActivityButtonsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.activitiesbuttonContainer, btnFragment).commit();
        //Testcode for adding logging functionallity


        if(API_KEY.isEmpty()){
            Toast.makeText(getApplicationContext(),"please obtain api key", Toast.LENGTH_LONG).show();
            return;
        }


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MatchRestResponse> call = apiService.getMatches();
        call.enqueue(new Callback<MatchRestResponse>() {
            @Override
            public void onResponse(Call<MatchRestResponse> call, Response<MatchRestResponse> response) {
                List<MatchRest> matches = response.body().getResults();
                Log.d(TAG,"" + matches.size());
            }

            @Override
            public void onFailure(Call<MatchRestResponse> call, Throwable t) {
                Log.e(TAG,t.toString());
            }
        });



    }


    //PROCESS FUNCTIONS
    //Function: GoalMade - press goal button to change view so you can select who scored
    public void goalMade(View view){
        /*
        Intent intent = new Intent(this, PlayerControl.class);

        //TEMPORARY CODE TO SHOWCASE CHRONO FUNCTIONALITY START
        matchTimer.stopChrono();
        intent.putExtra(EXTRA_MESSAGE, matchTimer.getTime());
        //TEMPORARY CODE TO SHOWCASE CHRONO FUNCTIONALITY END

        startActivity(intent);
        */
        testLog();

        //temporary to check change in score
        dc.getMatch().addScore(dc.getMatch().getTeam(0));

        teamsHeader.updateHeader();
        Log.i("game","Number of goals: " + String.valueOf(dc.getMatch().getScoreHome()));
        activities.updateActivities(1);

        matchTimer.stopChrono();
        loadPlayers();
        switchActivitiesButtonsToActionInfo();
    }

    //TEMP function to move to PlayerControl activity
    public void changePlayers(View view) {
        //PlayersFragment awayTeam = new PlayersFragment();
        //getSupportFragmentManager().beginTransaction().detach(faultAwayTeam).commit();
        //getSupportFragmentManager().beginTransaction().add(R.id.awayContainer, awayTeam).commit();

        awayTeam.setListPlayersByStatus(dc.getMatch().getTeam(0),Status.BENCHED);

        getSupportFragmentManager().beginTransaction().replace(R.id.awayContainer, awayTeam).commit();
    }

    //TEMP function to move to PlayerControl activity
    public void penaltyEvent(View view) {
        loadFaults();
    }

    public void faultU20(View view){
        loadPlayers();
    }

    //Function togglechrono - start/stop the chronometer - Clickable function
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void toggleChrono(View view){
        if(matchTimer.isChronoOn()){
            matchTimer.stopChrono();
        }else{
            resumeTimer();
            matchTimer.startChrono();
            resumeShotlock();

            //show the button again
            loadActivitiesButtons();
            loadFaults();
        }
    }

    /*
    public void pressTimer(View view){
        matchTimer.initTimer((TextView) findViewById(R.id.txtTimer), (long) (8*1000*60));
        matchTimer.getCdtTimer().start();

    }
    */

    //Shotlock gets paused when matchtimer is paused, shotlock has 2 buttons 1 button for home (shotlock resets countdown stays hometeam color) the other for away, (reset shotlock ,and set teamcolor)
    public void homeShotlock(View view){
        if(matchTimer.isChronoOn()){
            //Cancel first, to prevent another shotlock running in the bacground
            if(matchTimer!=null)
                matchTimer.getCdtShotlock().cancel();

            //re-initialize shot lock to set remaining time back to 30 sec
            matchTimer.initShotlock((TextView) findViewById(R.id.txtShotlock), (long) 30000);

            matchTimer.getCdtShotlock().start();

            findViewById(R.id.txtShotlock).setBackgroundColor(Color.WHITE);
        }
    }
    public void awayShotlock(View view){
        //Cancel first, to prevent another shotlock running in the bacground
        if(matchTimer.isChronoOn()){
            if(matchTimer!=null)
                matchTimer.getCdtShotlock().cancel();

            //re-initialize shot lock to set remaining time back to 30 sec
            matchTimer.initShotlock((TextView) findViewById(R.id.txtShotlock), (long) 30000);

            matchTimer.getCdtShotlock().start();

            findViewById(R.id.txtShotlock).setBackgroundColor(Color.BLUE);
        }

    }

    public void resumeTimer(){
        long timeRemaining = matchTimer.getTimeRemaining();
        matchTimer.initTimer((TextView) findViewById(R.id.txtTimer), timeRemaining);
        if(timeRemaining!=(8*1000*60)){
            matchTimer.getCdtTimer().start();
        }
    }

    public void resumeShotlock(){
        //re-initialize shotlock if necesary and start it
        Long timeRemaining = matchTimer.getShotlockTimeRemaining();
        matchTimer.initShotlock((TextView) findViewById(R.id.txtShotlock), timeRemaining);
        if(timeRemaining!=30000){
            matchTimer.getCdtShotlock().start();
        }

    }

    //Time out for each round each team can call time out once, cannot be paused, when clicked cannot be used again in same round for that team
    //2 buttons 1 for each team
    public void homeTimeout(View view){}
    public void awayTimeout(View view){}


    public void switchActivitiesButtonsToActionInfo(){
        if(infoFragment == null) {
            infoFragment = new ActivityInfoFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.activitiesbuttonContainer, infoFragment).commit();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.activitiesbuttonContainer, infoFragment).commit();

    }

    public void loadActivitiesButtons(){
        getSupportFragmentManager().beginTransaction().replace(R.id.activitiesbuttonContainer, btnFragment).commit();

    }

    public void loadFaults(){
        //First initialization of faultfragments
        if(faultAwayTeam==null || faultHomeTeam==null){
            faultHomeTeam = new FaultFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.homeContainer, faultHomeTeam).commit();

            faultAwayTeam = new FaultFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.awayContainer, faultAwayTeam).commit();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer, faultHomeTeam).commit();

        getSupportFragmentManager().beginTransaction().replace(R.id.awayContainer, faultAwayTeam).commit();

    }

    public void loadPlayers(){
        getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer, homeTeam).commit();

        getSupportFragmentManager().beginTransaction().replace(R.id.awayContainer, awayTeam).commit();
    }





    //start testcode log
    public void testLog(){
        dc.appendLog("Goal by (1)Home.","GH1","05:47",1);
        dc.appendLog("Goal by (5)Away.","GA1","04:02",1);
        dc.appendLog("Goal by (3)Home.","GH2","07:02",2);

       // dc.getSegmentedLog();
    }
    //end testcode log


}
