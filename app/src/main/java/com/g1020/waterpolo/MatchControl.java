package com.g1020.waterpolo;

import android.app.Application;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.*;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Domain.Status;
import application.ApplicationRuntime;
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

/**
 * Activity to process actions during a waterpolo match.
 */
public class MatchControl extends AppCompatActivity implements PlayersFragment.OnPlayerSelectedListener{

    private TeamsHeaderFragment teamsHeader;
    private PlayersFragment homeTeam;
    private PlayersFragment awayTeam;
    private ActivityFragment activities;

    private ApplicationRuntime ar;
    private Domaincontroller dc;
    private MatchTimer matchTimer;
    //Fault playerTimers
    private List<Player> faultPlayers = new ArrayList<>();
    boolean isBreak = false;

    //LIFECYCLE FUNCTIONS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_match_control);

        ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();
        //ar.setLocale("fr");

        //test code to see if function in activity can be called from the timerlistner in matchtimer
        dc.setCurrentActivity(this);

        //initialize shotlock timer
        matchTimer = ar.chronoSetup((TextView) findViewById(R.id.txtTimer), dc.getRoundTime(), dc.getBreakTime());
        matchTimer.initShotlock((TextView) findViewById(R.id.txtShotlock), (long) 30000);
        matchTimer.initTimer((TextView) findViewById(R.id.txtTimer), (dc.getRoundTime()*1000*60));

        // Creating fragments for this activity
        teamsHeader = new TeamsHeaderFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.teamsheadercontainer, teamsHeader).commit();

        homeTeam = PlayersFragment.newInstance(0);
        getSupportFragmentManager().beginTransaction().add(R.id.homeContainer, homeTeam).commit();
        awayTeam = PlayersFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction().add(R.id.awayContainer, awayTeam).commit();
        homeTeam.setOtherTeam(awayTeam);
        awayTeam.setOtherTeam(homeTeam);

        ButtonsFragment btnFragment = new ButtonsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.buttonsContainer, btnFragment).commit();
        TimeOutFragment timeOutFragment = new TimeOutFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.timeoutcontainer, timeOutFragment).commit();
        ShotClockFragment shotClockFragment = new ShotClockFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.shotclockcontainer, shotClockFragment).commit();

        //setting up the round, creating the activitylog and providing start-entry in log
        int round = dc.getMatch().getCurrentRound();
        dc.appendLog("Round " + round  + " started","SR" + round,dc.getMatch().getHomeTeam().getDivision().getRoundLengthS(),round); //relocate to startchrono for first time only here for testing
        activities = ActivityFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction().add(R.id.activitiesContainer, activities).commit();

        dc.startMatch();
    }

    //PROCESS FUNCTIONS
    /**
     * Method that is called when goal button is pressed.
     * This method adds a goal to the scoring team, updates the matchheader and adds the goal to the log.
     *
     * @param view The view it is associated with
     */
    public void goalMade(View view){

        Player sp = dc.getSelectedPlayer();
        if(sp!=null && sp.getStatus() != Status.GAMEOVER){
            //add the goal to the current selected player in domaincontroller
            dc.addGoal();

            //Logging
            addToLog(sp, "G","Goal by " + dc.getSelectedPlayer().getFullName());

            teamsHeader.updateHeader();
            activities.updateActivities(dc.getMatch().getCurrentRound());

            clearSelectedPlayer();

            if(!this.isBreak)
                toggleChrono(view);

        }else {
            toast("Select a player first.");
        }

    }

    /**
     * Method that is called when switch button is pressed.
     * Select a player, press the button, select another player.
     * This method changes the capnumbers of the two selected players.
     *
     * @param view The view it is associated with
     */
    public void changePlayers(View view) {

        Player sp = dc.getSelectedPlayer();

        if(sp!=null){
            if(dc.getPlayerToSwitch()==null){
                dc.setPlayerToSwitch(sp);
                if(!this.isBreak)
                    stopShotlock(view);
            }else{
                dc.setPlayerToSwitch(null);//reset for next switch

                if(!this.isBreak)
                    toggleChrono(view);
            }
            clearSelectedPlayer();
        }else {
            toast("Select a player first.");
        }

    }

    /**
     * Method that is called when injury button is pressed.
     * After selecting a player, this method adds an injury to the selected player , updates the matchheader and adds the injury to the log.
     *
     * @param view The view it is associated with.
     */
    public void injurySustained(View view){

        Player sp = dc.getSelectedPlayer();
        if(sp!=null){

            dc.addInjury();

            addToLog(sp, "I",dc.getSelectedPlayer().getFullName() + " got injured");
            activities.updateActivities(dc.getMatch().getCurrentRound());

            updateBackgroundPlayer(sp);
            clearSelectedPlayer();

            if(!this.isBreak){
                toggleChrono(view);
            }

        }else {
            toast("Select a player first.");
        }

    }

    /**
     * Method that is called when U20, a standard fault in waterpolo, button is pressed.
     * After selecting a player, this method adds a U20 to the selected player , updates the player tile and adds the fault to the log.
     * The selected player receives 20 seconds punishment.
     *
     * @param view The view it is associated with.
     */
    public void faultU20(View view){

        //FIRST CHECK IF PLAYER ALREADY HAS 20 sec FAULT if not ignore press of button or give message player allready punished

        //If players selected preform action
        if(dc.getSelectedPlayer()!=null){
            Player sp = dc.getSelectedPlayer();
            if(!faultPlayers.contains(sp)){
                //add the fault to the current selected player in domaincontroller
                dc.addFaultU20();

                //update the background color
                updateBackgroundPlayer(sp);

                //Logging
                addToLog(sp, "U","Fault U20 for " + sp.getFullName() + ".");

                clearSelectedPlayer();      //clear selected player from layout

                if(!this.isBreak)
                    toggleChrono(view);

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

    /**
     * Method that is called when UMV, a heavy fault in waterpolo, button is pressed.
     * After selecting a player, this method adds a UMV to the selected player , updates the player tile and adds the fault to the log.
     * The selected player is excluded from the game.
     *
     * @param view The view it is associated with.
     */
    public void faultUMV(View view) {

        Player sp = dc.getSelectedPlayer();
        if(sp!=null){

            dc.addFaultUMV();

            addToLog(sp, "UMV","Fault UMV for " + sp.getFullName() + ".");
            activities.updateActivities(dc.getMatch().getCurrentRound());

            updateBackgroundPlayer(sp);

            clearSelectedPlayer();

            if(!this.isBreak)
                toggleChrono(view);

        }else {
            toast("Select a player first.");

        }
    }

    /**
     * Method that is called when UMV4, a brutality in waterpolo, button is pressed.
     * After selecting a player, this method adds a UMV4 to the selected player, updates the player tile and adds the fault to the log.
     * The selected player is excluded from the game, but can only be replaced after 4 minutes.
     *
     * @param view The view it is associated with.
     */
    public void faultUMV4(View view) {

        Player sp = dc.getSelectedPlayer();
        if(sp!=null){

            dc.addFaultUMV4();

            addToLog(sp, "UMV4","Fault UMV4 for " + sp.getFullName() + ".");
            activities.updateActivities(dc.getMatch().getCurrentRound());

            updateBackgroundPlayer(sp);

            clearSelectedPlayer();

            if(!this.isBreak)
                toggleChrono(view);

        }else {
            toast("Select a player first.");
        }

    }

    //sets the game back to the selected action. everything gets deleted except BRUTALITIES
    public void revertToAction(View view) {
        //user gets 2 input fields in view minutes field and seconds view
        //this value is the time we want to revert to

        /*
        //matchtimer reset - using placeholder values until view is in order
        long min = 8;
        long sec = 0;
        TextView txtTime = (TextView) findViewById(R.id.txtTimer);
        //check layout element if input is valid and filled in
        //TODO: update from customer, require to work with local DB during match, keep code as placeholder for future but cannot be used right now to revert.

        //calc timeremaing matchtimer
        long millisecondremaining = ((min*60*1000)+(sec*1000));
        matchTimer.initShotlock((TextView) findViewById(R.id.txtShotlock), millisecondremaining);

        //use txtshotlock to get new string value
        String revertime = txtTime.getText().toString();

        //get log to revert to
        int i = dc.getLogIndex(revertime);

        //if no revertlog found return 1 show message cannot be reverted
        if(i==-1){
            toast("Cannot be reverted, check validity of input.");
        }else{



        }
        */

        //jump to endadministration
        finishMatch();

    }

    //deletes the last added action. //Maybe also possible to undo method revertToAction()
    /**
     * Method that is called when the user presses the button undo. It removes the last action from the log and updates the log.
     *
     * @param view The view it is associated with.
     */
    public void undoAction(View view) {
        dc.undoLog();
        activities.updateActivities(dc.getMatch().getCurrentRound());
    }

    /**
     * Method that is called when the matchtimer or shotclock is pressed, used to pauze match and halt the shotlock
     *
     * @param view The view it is associated with.
     */
    public void toggleChrono(View view){
        if(matchTimer.isChronoOn()){
            matchTimer.stopChrono();
        }else{
            resumeTimer();
            resumeShotlock();
            matchTimer.startChrono();
        }
    }

    /**
     * Method that is called when the reset button is pressed.
     * This method resets and starts the shotlock, but keeps the matchtimer running.
     *
     * @param view The view it is associated with.
     */
    public void shotlock(View view){

        if(!matchTimer.isTimoutUsed()){             //No timeout used = normal process
            //Cancel first, to prevent another shotlock running in the bacground
            if(matchTimer!=null)
                matchTimer.getCdtShotlock().cancel();
            matchTimer.stopChrono();

            //re-initialize shot lock to set remaining time back to 30 sec
            matchTimer.initShotlock((TextView) findViewById(R.id.txtShotlock), (long) 30000);
            matchTimer.initTimer((TextView) findViewById(R.id.txtTimer), matchTimer.getTimeRemaining());

            matchTimer.getCdtShotlock().start();
            matchTimer.startChrono();

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

    /**
     * Method to pauze the match.
     *
     * @param view The view it is associated with.
     */
    public void stopShotlock(View view){
        //Stop matchTimer
        matchTimer.stopChrono();

        //reset shotlock to 30 seconds
        //Cancel first, to prevent another shotlock running in the background

        //if(matchTimer!=null)
            matchTimer.getCdtShotlock().cancel();

        //re-initialize shot lock to set remaining time back to 30 sec
        TextView txtShotlock = (TextView) findViewById(R.id.txtShotlock);
        matchTimer.initShotlock(txtShotlock, (long) 30000);
        txtShotlock.setText("30");

        //Stop faulttimers
        toggleFaultTimers(false);

    }

    /**
     * Method that restarts the matchtimer
     */
    private void resumeTimer(){
        long timeRemaining = matchTimer.getTimeRemaining();
        matchTimer.initTimer((TextView) findViewById(R.id.txtTimer), timeRemaining);
        if(timeRemaining!=(8*1000*60)){
            matchTimer.getCdtTimer().start();
        }
    }

    /**
     * Method that resumes the shotlock
     */
    private void resumeShotlock(){
        //re-initialize shotlock if necesary and start it
        Long timeRemaining = matchTimer.getShotlockTimeRemaining();
        matchTimer.initShotlock((TextView) findViewById(R.id.txtShotlock), timeRemaining);
        if(timeRemaining!=30000){
            matchTimer.getCdtShotlock().start();
        }

    }

    /**
     * Method that initializes and starts the break period.
     */
    public void prepareBreak(){
        //clear matchtimer
        matchTimer.clearTimer();
        //setup breaktimer view
        matchTimer.initBreak((TextView) findViewById(R.id.txtTimer));
        matchTimer.startBreak();
        this.isBreak = true;
        //disable certain buttons on screen - timout buttons, shotlock, timertext, goal
        disableActions();
    }

    /**
     * Method that starts a new round and resets the timers after the break.
     * When all 4 quarters are finished the end administration activity is called
     */
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
        this.isBreak = false;
    }

    /**
     * This method sends a message to the backend that the match has ended and shows the end administration activity
     */
    public void finishMatch(){
        //post to the backend that the match ended
        dc.endMatch();

        Intent intent = new Intent(this, AdministrationEnd.class);
        startActivity(intent);

    }

    /**
     * Method to disable buttons in matchcontrol.
     */
    public void disableActions(){
        //get all actions to disable
        Button btnTimoutHome = (Button) findViewById(R.id.btnTimeOutHome);
        Button btnTimoutAway = (Button) findViewById(R.id.btnTimeOutAway);
        TextView txtTimer = (TextView) findViewById(R.id.txtTimer);
        TextView txtShotlock = (TextView) findViewById(R.id.txtShotlock);
        TextView txtResetShotlock = (TextView) findViewById(R.id.txtResetShotlock);
        //disable buttons
        btnTimoutHome.setClickable(false);
        btnTimoutAway.setClickable(false);
        txtTimer.setEnabled(false);
        txtShotlock.setEnabled(false);
        txtResetShotlock.setEnabled(false);

    }

    /**
     * Method to enable buttons in matchcontrol.
     */
    public void enableActions(){
        //get all actions to disable
        TextView txtTimer = (TextView) findViewById(R.id.txtTimer);
        TextView txtShotlock = (TextView) findViewById(R.id.txtShotlock);
        TextView txtResetShotlock = (TextView) findViewById(R.id.txtResetShotlock);
        //enable buttons
        resetTimout();
        txtTimer.setEnabled(true);
        txtShotlock.setEnabled(true);
        txtShotlock.setText("30");
        txtResetShotlock.setEnabled(true);

    }

    /**
     * Method that is called when the home timout button is pressed.
     * Each team can use the time out once every round, timeout cannot be paused.
     *
     * @param view The view it is associated with.
     */
    public void homeTimeout(View view){
        matchTimer.initTimeout((Button) findViewById(R.id.btnTimeOutHome));
        matchTimer.getCdtTimout().start();

        //ADD FUNCTION TO LIMIT CLICKABILITY
        disableActions();

        //stop matchTimer
        matchTimer.stopChrono();
        //stop shotlock
        if(matchTimer!=null)
            matchTimer.getCdtShotlock().cancel();

    }

    /**
     * Method that is called when the home timout button is pressed.
     * Each team can use the time out once every round, timeout cannot be paused.
     *
     * @param view The view it is associated with.
     */
    public void awayTimeout(View view){
        matchTimer.initTimeout((Button) findViewById(R.id.btnTimeOutAway));
        matchTimer.getCdtTimout().start();

        //ADD FUNCTION TO LIMIT CLICKABILITY
        disableActions();

        //stop matchTimer
        matchTimer.stopChrono();
        //stop shotlock
        if(matchTimer!=null)
            matchTimer.getCdtShotlock().cancel();
    }

    /**
     * Reinitializes the timeout buttons after a break
     */
    public void resetTimout(){
        Button btnTimoutHome = (Button) findViewById(R.id.btnTimeOutHome);
        Button btnTimoutAway = (Button) findViewById(R.id.btnTimeOutAway);
        btnTimoutHome.setClickable(true);
        btnTimoutAway.setClickable(true);
        btnTimoutHome.setText("TIMEOUT HOME");
        btnTimoutAway.setText("TIMEOUT AWAY");
    }

    /**
     * Method to update the background image of the playertiles in the playerfragments.
     * Depending on the selected player, this method calls selects the playerfragment where the player resides
     *
     * @param sp the player who's tile needs to be updated
     */
    private void updateBackgroundPlayer(Player sp){
        if(sp.getTeam().equals(dc.getHomeTeam())){
            homeTeam.updateBackgroundPlayer();
        } else {
            awayTeam.updateBackgroundPlayer();
        }
    }

    /**
     * Reload the playerfragments to display changes in the playertiles
     */
    public void ReloadFragments(){

        homeTeam = PlayersFragment.newInstance(0);
        getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer, homeTeam).commit();
        awayTeam = PlayersFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction().replace(R.id.awayContainer, awayTeam).commit();
        homeTeam.setOtherTeam(awayTeam);
        awayTeam.setOtherTeam(homeTeam);
    }

    /**
     * Method to clear the selectedPlayer after an action has ben assigned to him.
     */
    public void clearSelectedPlayer(){
        homeTeam.resetFontPlayers();
        awayTeam.resetFontPlayers();
        dc.resetSelectedPlayer();
    }

    /**
     * Method to control all of the faulttimers through the use of the shotlock
     *
     * @param start
     */
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


    /**
     * Method to add entries in the log.
     *
     * @param sp the player who has performed the action.
     * @param event the type of action that has been performed. This type is a part of the eventcode, which is used to classify the different actions in the log.
     * @param description a description of the action that has been performed, displayed in the log.
     */
    public void addToLog(Player sp, String event, String description){
        //Determine home or away team
        String t;
        if(sp.getTeam().equals(dc.getHomeTeam())){t = "H";}else {t = "A";}
        //Get time notation
        TextView tt = (TextView) findViewById(R.id.txtTimer);
        dc.appendLog(description,event + t + dc.getMatch().getCurrentRound(), tt.getText().toString(),dc.getMatch().getCurrentRound());
    }

    /**
     * Method to remove the latest event log.
     */
    public void undoLatest(View view){
        //get latest log use it to get what activity happend and undo the effect of that activity

        //Remove undone event from log
        dc.undoLog();
        activities.updateActivities(dc.getMatch().getCurrentRound());
    }

    /**
     * Method to set up the toast notification in matchcontrol.
     */
    public void toast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    /**
     * Method to pass the selected player to the domaincontroller
     */
    @Override
    public void onArticleSelected(Boolean hometeam, int playerId) {
        dc.setSelectedPlayer(hometeam, playerId);
    }

}
