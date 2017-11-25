package com.g1020.waterpolo;

import android.os.CountDownTimer;
import android.support.v7.app.*;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Application.ApplicationRuntime;
import Domain.Division;
import Domain.Domaincontroller;
import Domain.MatchTimer;
import Domain.PenaltyType;
import Domain.Player;
import persistency.GoalRest;
import persistency.MatchRest;
import persistency.PlayerRest;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchControl extends AppCompatActivity implements PlayersFragment.OnPlayerSelectedListener{

    //backend variables
    ApiInterface apiService;
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

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.logo_waterpolo);

        ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();
        apiService = dc.getApiService();

        //test code to see if function in activity can be called from the timerlistner in matchtimer
        dc.setCurrentActivity(this);

        // PIETER
        dc.startMatch();
        //Division heren = new Division("Eerste klasse Heren", 8,2);
        //dc.createTeams("Oostende", heren,"Aalst",heren);
        //dc.createPlayers();
        // END PIETER

        matchTimer = ar.chronoSetup((TextView) findViewById(R.id.txtTimer), dc.getRoundTime(), dc.getBreakTime());

        //initialize shotlock timer
        matchTimer.initShotlock((TextView) findViewById(R.id.txtShotlock), (long) 30000);
        matchTimer.initTimer((TextView) findViewById(R.id.txtTimer), (dc.getRoundTime()*1000*60));

        teamsHeader = new TeamsHeaderFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.teamsheadercontainer, teamsHeader).commit();

        homeTeam = PlayersFragment.newInstance(0);
        getSupportFragmentManager().beginTransaction().add(R.id.homeContainer, homeTeam).commit();

        awayTeam = PlayersFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction().add(R.id.awayContainer, awayTeam).commit();

        homeTeam.setOtherTeam(awayTeam);
        awayTeam.setOtherTeam(homeTeam);

        int round = dc.getMatch().getCurrentRound();
        dc.appendLog("Round " + round  + " started","SR" + round,dc.getMatch().getHomeTeam().getDivision().getRoundLengthS(),round); //relocate to startchrono for first time only here for testing
        activities = ActivityFragment.newInstance(1);

        getSupportFragmentManager().beginTransaction().add(R.id.activitiesContainer, activities).commit();

        btnFragment = new ActivityButtonsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.activitiesbuttonContainer, btnFragment).commit();

        //Testcode for adding logging functionallity

    }

    @Override
    protected void onResume(){
        super.onResume();

        btnFragment.getTxtShotClock().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                stopShotlock(v);
                Log.i("game","Shotclock reset");
                return true;
            }
        });

    }
    //PROCESS FUNCTIONS
    //Function: GoalMade - press goal button to change view so you can select who scored
    public void goalMade(View view){

        Player sp = dc.getSelectedPlayer();
        if(sp!=null){
            //add the goal to the current selected player in domaincontroller
            dc.addGoal();

            //Post goal to live
            dc.asyncPostGoal(sp);

            //Logging
            addToLog(sp, "G","Goal by " + dc.getSelectedPlayer().getFullName());
            clearSelectedPlayer();

            teamsHeader.updateHeader();
            activities.updateActivities(dc.getMatch().getCurrentRound());

            stopShotlock(view);

            //loadPlayers();
        }else {
            toast("Select a player first.");
        }
    }

    //TEMP function to move to PlayerControl activity
    public void changePlayers(View view) {

        Player sp = dc.getSelectedPlayer();
        if(sp!=null){

            dc.switchPlayerCaps();
            clearSelectedPlayer();

        }else {
            toast("Select a player first.");
        }
        //PlayersFragment awayTeam = new PlayersFragment();
        //getSupportFragmentManager().beginTransaction().detach(faultAwayTeam).commit();
        //getSupportFragmentManager().beginTransaction().add(R.id.awayContainer, awayTeam).commit();

    }

    public void faultU20(View view){

        //FIRST CHECK IF PLAYER ALREADY HAS 20 sec FAULT if not ignore press of button or give message player allready punished

        //If players selected preform action
        if(dc.getSelectedPlayer()!=null){
            Player sp = dc.getSelectedPlayer();
            if(!faultPlayers.contains(sp)){
                //add the fault to the current selected player in domaincontroller
                dc.addFaultU20();
                //post fault
                dc.asyncPostFault(sp, PenaltyType.U20);

                //update the background color
                updateBackgroundPlayer(sp);

                //Logging
                addToLog(sp, "U","Fault U20 for " + sp.getFullName() + ".");

                loadPlayers();
                clearSelectedPlayer();      //clear selected player from layout

                activities.updateActivities(dc.getMatch().getCurrentRound());

                //Start 20 second timer
                sp.setFaultTimer();
                CountDownTimer faultTimer = sp.getFaultTimer();
                if(matchTimer.isChronoOn()){
                    faultTimer.start();
                }
                faultPlayers.add(sp);                               //List of all players with faultTimers

            }else if(sp.getFaultTimeRemaining()==0){
                faultPlayers.remove(sp);
            }
            else {
                //Player is already punished by this fault he cannot get extra
                toast("player " + sp.getFullName() + " still has an ongoing U20 Fault");
            }
        }else {
            toast("Select a player first.");
        }

    }

    public void faultUMV(View view) {



        Player sp = dc.getSelectedPlayer();
        if(sp!=null){

            dc.addFaultUMV();
            //post fault
            dc.asyncPostFault(sp, PenaltyType.UMV);

            addToLog(sp, "UMV","Fault UMV for " + sp.getFullName() + ".");
            activities.updateActivities(dc.getMatch().getCurrentRound());

            updateBackgroundPlayer(sp);

            clearSelectedPlayer();

        }else {
            toast("Select a player first.");

        }
    }

    public void faultUMV4(View view) {

        Player sp = dc.getSelectedPlayer();
        if(sp!=null){

            dc.addFaultUMV4();
            //post fault
            dc.asyncPostFault(sp, PenaltyType.UMV4);

            activities.updateActivities(dc.getMatch().getCurrentRound());
            addToLog(sp, "UMV4","Fault UMV for " + sp.getFullName() + ".");
            updateBackgroundPlayer(sp);

            clearSelectedPlayer();

        }else {
            toast("Select a player first.");
        }

    }

    //sets the game back to the selected action. everything gets deleted except BRUTALITIES
    public void revertToAction(View view) {
    }

    //deletes the last added action. //Maybe also possible to undo method revertToAction()
    public void undoAction(View view) {
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
                toggleChrono(view);

            //re-initialize shot lock to set remaining time back to 30 sec
            matchTimer.initShotlock((TextView) findViewById(R.id.txtShotlock), (long) 30000);
            matchTimer.initTimer((TextView) findViewById(R.id.txtTimer), matchTimer.getTimeRemaining());

            matchTimer.getCdtShotlock().start();
            toggleChrono(view);


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

    public void prepareBreak(){
        //clear matchtimer
        matchTimer.clearTimer();
        //setup breaktimer view
        matchTimer.initBreak((TextView) findViewById(R.id.txtTimer));
        matchTimer.startBreak();

        //disable certain buttons on screen - timout buttons, shotlock, timertext, goal
        disableActions();


    }

    public void setupNewRound(){

        //check if round = 5 yes go to finishscreen
        if(dc.getMatch().getCurrentRound()>4){
            finishMatch();
        }else{
            //log new round start
            int round = dc.getMatch().getCurrentRound();
            dc.appendLog("Round " + round  + " started","SR" + round,dc.getMatch().getHomeTeam().getDivision().getRoundLengthS(),round);
            //setup matchtimer view
            matchTimer = ar.chronoSetup((TextView) findViewById(R.id.txtTimer), dc.getRoundTime(), dc.getBreakTime());
            matchTimer.initShotlock((TextView) findViewById(R.id.txtShotlock), (long) 30000);
            matchTimer.initTimer((TextView) findViewById(R.id.txtTimer), (dc.getRoundTime()*1000*60));
            //update round view - to update currentround in view
            teamsHeader.updateHeader();
            activities.updateActivities(dc.getMatch().getCurrentRound());

            //reenable certain buttons on screen
            enableActions();

            //reenable timouttimers and reset them (break onfinish already ups roundvalue)
        }
    }

    public void finishMatch(){

    }

    public void disableActions(){
        //get all actions to disable
        Button btnTimoutHome = (Button) findViewById(R.id.btnTimeOutHome);
        Button btnTimoutAway = (Button) findViewById(R.id.btnTimeOutAway);
        TextView txtTimer = (TextView) findViewById(R.id.txtTimer);
        TextView txtShotlock = (TextView) findViewById(R.id.txtShotlock);
        //disable buttons
        btnTimoutHome.setClickable(false);
        btnTimoutAway.setClickable(false);
        txtTimer.setEnabled(false);
        txtShotlock.setEnabled(false);

    }
    //re enable actions
    public void enableActions(){
        //get all actions to disable
        TextView txtTimer = (TextView) findViewById(R.id.txtTimer);
        TextView txtShotlock = (TextView) findViewById(R.id.txtShotlock);
        //disable buttons
        resetTimout();
        txtTimer.setEnabled(true);
        txtShotlock.setEnabled(true);
        txtShotlock.setText("30");

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
    public void resetTimout(){
        Button btnTimoutHome = (Button) findViewById(R.id.btnTimeOutHome);
        Button btnTimoutAway = (Button) findViewById(R.id.btnTimeOutAway);
        btnTimoutHome.setClickable(true);
        btnTimoutAway.setClickable(true);
        btnTimoutHome.setText("TIMEOUT HOME");
        btnTimoutAway.setText("TIMEOUT AWAY");
    }

    private void updateBackgroundPlayer(Player sp){
        if(sp.getTeam().equals(dc.getHomeTeam())){
            homeTeam.updateBackgroundPlayer();
        } else {
            awayTeam.updateBackgroundPlayer();
        }
    }
    //Function to clear selectedPlayer after performing buttonAction
    public void clearSelectedPlayer(){
        homeTeam.resetFontPlayers();
        awayTeam.resetFontPlayers();
        dc.resetSelectedPlayer();
    }

    //Function to control all faultimers via shotlock
    public void toggleFaultTimers(boolean start){
        if(faultPlayers!=null) {
            for (int i = 0; i < faultPlayers.size(); i++) {
                if (start) {
                    if(faultPlayers.get(i).getFaultTimeRemaining()!=0){
                        faultPlayers.get(i).getFaultTimer().start();
                    }else {
                        faultPlayers.remove(faultPlayers.get(i));
                    }
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





    //Log actions
    public void addToLog(Player sp, String event, String description){
        //Determine home or away team
        String t;
        if(sp.getTeam().equals(dc.getHomeTeam())){t = "H";}else {t = "A";}
        //Get time notation
        TextView tt = (TextView) findViewById(R.id.txtTimer);
        dc.appendLog(description,event + t + dc.getMatch().getCurrentRound(), tt.getText().toString(),dc.getMatch().getCurrentRound());
    }
    //Remove latest event log
    public void undoLatest(View view){
        //get latest log use it to get what activity happend and undo the effect of that activity

        //Remove undone event from log
        dc.undoLog();
        activities.updateActivities(dc.getMatch().getCurrentRound());
    }
    //Setup toast notification
    public void toast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    // sets the selected player in the domaincontroller
    @Override
    public void onArticleSelected(Boolean hometeam, int playerId) {
        dc.setSelectedPlayer(hometeam, playerId);
    }


    //end testcode log
    public void testFunction(){
        Log.d("SHARED TEST CALL", "Function was succesfully called");
    }

}
