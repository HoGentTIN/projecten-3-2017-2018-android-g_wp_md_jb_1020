package com.g1020.waterpolo;

import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.*;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Application.ApplicationRuntime;
import Domain.CompetitionClass;
import Domain.Domaincontroller;
import Domain.MatchTimer;
import Domain.Player;

public class MatchControl extends AppCompatActivity implements PlayersFragment.OnPlayerSelectedListener{

    private static final String TAG = MatchControl.class.getSimpleName();
    private final static String API_KEY = "";

    ApplicationRuntime ar;  //this adds temporary code to this class
    Domaincontroller dc;
    MatchTimer matchTimer;

    TeamsHeaderFragment teamsHeader;
    PlayersFragment homeTeam;
    PlayersFragment awayTeam;

    ActivityFragment activities;
    ActivityInfoFragment infoFragment;
    ActivityButtonsFragment btnFragment;

    //Fault playerTimers
    List<Player> faultPlayers = new ArrayList<>();

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

        homeTeam.setOtherTeam(awayTeam);
        awayTeam.setOtherTeam(homeTeam);

        dc.appendLog("Round 1 started","SR1","8:00",1); //relocate to startchrono for first time only here for testing
        activities = ActivityFragment.newInstance(1);

        getSupportFragmentManager().beginTransaction().add(R.id.activitiesContainer, activities).commit();

        btnFragment = new ActivityButtonsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.activitiesbuttonContainer, btnFragment).commit();
        //Testcode for adding logging functionallity


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

        //add the goal to the current selected player in domaincontroller
        dc.addGoal();
     //   dc.appendLog("Goal by " + dc.getSelectedPlayer().getFullName(),"GH1");

        teamsHeader.updateHeader();
        activities.updateActivities(1);

        matchTimer.stopChrono();

        //loadPlayers();

    }

    public void penaltyMade(View view) {

    }

    //TEMP function to move to PlayerControl activity
    public void changePlayers(View view) {
        //PlayersFragment awayTeam = new PlayersFragment();
        //getSupportFragmentManager().beginTransaction().detach(faultAwayTeam).commit();
        //getSupportFragmentManager().beginTransaction().add(R.id.awayContainer, awayTeam).commit();

    }

    public void faultU20(View view){

        //FIRST CHECK IF PLAYER ALREADY HAS 20 sec FAULT if not ignore press of button or give message player allready punished

        //If players selected preform action
        if(dc.getSelectedPlayer()!=null){
            Player sp = dc.getSelectedPlayer();
            //add the fault to the current selected player in domaincontroller
            dc.addFaultU20();
            loadPlayers();
            clearSelectedPlayer();

            //Start 20 second timer
            sp.setFaultTimer();
            CountDownTimer faultTimer = sp.getFaultTimer();
            if(matchTimer.isChronoOn()){
                faultTimer.start();
            }
            faultPlayers.add(sp);

        }

    }

    //Function togglechrono - backup function to pauze match and simply halt the shotlock
    public void toggleChrono(View view){
        if(matchTimer.isChronoOn()){
            matchTimer.stopChrono();
        }
            //show the button again
            loadActivitiesButtons();


        loadPlayers();
    }


    //Shotlock button starts matchtimer and shotlocktimer, on press press reset shotlock but keep matchTimer running
    public void shotlock(View view){

        if(!matchTimer.isTimoutUsed()){             //No timeout used = normal process
            //Cancel first, to prevent another shotlock running in the bacground
            if(matchTimer!=null)
                matchTimer.getCdtShotlock().cancel();

            //re-initialize shot lock to set remaining time back to 30 sec
            matchTimer.initShotlock((TextView) findViewById(R.id.txtShotlock), (long) 30000);
            matchTimer.getCdtShotlock().start();

        }else {
            resumeShotlock();           //restart shotlock on the time it had before start of timeout
            matchTimer.resetIsTimoutUsed();
        }

        //needs to be done otherwise it would keep creating new timer
        if(!matchTimer.isChronoOn()){
            resumeTimer();
            matchTimer.startChrono();
        }

        //Restart Faulttimers
        toggleFaultTimers(true);


    }

    //Shotlock txtfield pressed to pauze match
    public void stopShotlock(View view){
        //Stop matchTimer
        matchTimer.stopChrono();

        //reset shotlock to 30 seconds
        //Cancel first, to prevent another shotlock running in the bacground
        if(matchTimer!=null)
            matchTimer.getCdtShotlock().cancel();

        //re-initialize shot lock to set remaining time back to 30 sec
        TextView txtShotlock = (TextView) findViewById(R.id.txtShotlock);
        matchTimer.initShotlock(txtShotlock, (long) 30000);
        txtShotlock.setText("30");

        //Stop faulttimers
        toggleFaultTimers(false);

    }

    //Function to restart the timer correctly
    public void resumeTimer(){
        long timeRemaining = matchTimer.getTimeRemaining();
        matchTimer.initTimer((TextView) findViewById(R.id.txtTimer), timeRemaining);
        if(timeRemaining!=(8*1000*60)){
            matchTimer.getCdtTimer().start();
        }
    }

    //Function for when matchtimer pauze was used
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
    public void homeTimeout(View view){
        matchTimer.initTimeout((Button) findViewById(R.id.btnTimeOutHome));
        matchTimer.getCdtTimout().start();

        //ADD FUNCTION TO LIMIT CLICKABILITY

        //stop matchTimer
        matchTimer.stopChrono();
        //stop shotlock
        if(matchTimer!=null)
            matchTimer.getCdtShotlock().cancel();

    }
    public void awayTimeout(View view){
        matchTimer.initTimeout((Button) findViewById(R.id.btnTimeOutAway));
        matchTimer.getCdtTimout().start();

        //ADD FUNCTION TO LIMIT CLICKABILITY

        //stop matchTimer
        matchTimer.stopChrono();
        //stop shotlock
        if(matchTimer!=null)
            matchTimer.getCdtShotlock().cancel();
    }

    //Function to clear selectedPlayer after performing buttonAction
    public void clearSelectedPlayer(){
        homeTeam.resetFontPlayers();
        awayTeam.resetFontPlayers();
    }

    //Function to control all faultimers via shotlock
    public void toggleFaultTimers(boolean start){
        if(faultPlayers!=null) {

            for (int i = 0; i < faultPlayers.size(); i++) {
                Log.d("sizetes",i +""+faultPlayers.size());
                if (start) {
                    faultPlayers.get(i).getFaultTimer().start();
                } else {
                    faultPlayers.get(i).getFaultTimer().cancel();
                    faultPlayers.get(i).setFaultTimer();            //Stores faultimers for correct time reactivation
                }
            }
        }
    }

    public void showActionInfo(){
        if(infoFragment == null) {
            infoFragment = new ActivityInfoFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.activitiesbuttonContainer, infoFragment).commit();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.activitiesbuttonContainer, infoFragment).commit();

    }

    public void loadActivitiesButtons(){
        getSupportFragmentManager().beginTransaction().replace(R.id.activitiesbuttonContainer, btnFragment).commit();

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

    // sets the selected player in the domaincontroller
    @Override
    public void onArticleSelected(Boolean hometeam, int playerId) {
        dc.setSelectedPlayer(hometeam, playerId);
    }
    //end testcode log


}
