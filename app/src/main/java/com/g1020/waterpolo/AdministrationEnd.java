package com.g1020.waterpolo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Domain.Player;
import application.ApplicationRuntime;
import Domain.Domaincontroller;
import views.FontManager;

/**
 * Activity that displays an overview for the played match.
 */
public class AdministrationEnd extends AppCompatActivity implements PlayersFragmentSingleList.OnPlayerSelectedListener {

    private TeamsHeaderFragment teamsHeaderFragment;
    private PlayersFragmentSingleList homeTeam;
    private PlayersFragmentSingleList awayTeam;

    private ListView lvActivitiesQ4;
    private List q4;

    private TextInputEditText txtInputLayName, txtInputLayCode;

    private Domaincontroller dc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administration_end);

        ApplicationRuntime ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();
        dc.getMatch().setCurrentRound(4);
        dc.setCurrentActivity(this);

        txtInputLayName = (TextInputEditText) findViewById(R.id.txtInputLayName);
        txtInputLayCode = (TextInputEditText) findViewById(R.id.txtInputLayCode);

        ButtonsFragment btnFragment = new ButtonsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.adminbuttonsContainer, btnFragment).commit();

        teamsHeaderFragment = new TeamsHeaderFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.teamsheadercontainer, teamsHeaderFragment).commit();

        homeTeam = PlayersFragmentSingleList.newInstance(0);
        getSupportFragmentManager().beginTransaction().add(R.id.homeContainer, homeTeam).commit();

        awayTeam = PlayersFragmentSingleList.newInstance(1);
        getSupportFragmentManager().beginTransaction().add(R.id.awayContainer, awayTeam).commit();

        homeTeam.setOtherTeam(awayTeam);
        awayTeam.setOtherTeam(homeTeam);

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

        ListView lvActivitiesQ1 = (ListView) findViewById(R.id.activitiesContainerFirstRound);
        List q1 = dc.getLogForRound(1);
        lvActivitiesQ1.setAdapter(new ArrayAdapter<String>(AdministrationEnd.this,
                android.R.layout.simple_list_item_1, q1));

        ListView lvActivitiesQ2 = (ListView) findViewById(R.id.activitiesContainerSecondRound);
        List q2 = dc.getLogForRound(2);
        lvActivitiesQ2.setAdapter(new ArrayAdapter<String>(AdministrationEnd.this,
                android.R.layout.simple_list_item_1, q2));

        ListView lvActivitiesQ3 = (ListView) findViewById(R.id.activitiesContainerThirdRound);
        List q3 = dc.getLogForRound(3);
        lvActivitiesQ3.setAdapter(new ArrayAdapter<String>(AdministrationEnd.this,
                android.R.layout.simple_list_item_1, q3));

        lvActivitiesQ4 = (ListView) findViewById(R.id.activitiesContainerFourthRound) ;
        q4 = dc.getLogForRound(4);
        lvActivitiesQ4.setAdapter(new ArrayAdapter<String>(AdministrationEnd.this,
                android.R.layout.simple_list_item_1, q4));

        TextView txtIconSign = (TextView) findViewById(R.id.iconSignMatch);
        setIconFont(txtIconSign);
    }

    /**
     * Method to sign the match after the referee has entered his credentials.
     */
    public void signMatch(View view) throws InterruptedException {

        String pss = txtInputLayCode.getText().toString();
        String email = txtInputLayName.getText().toString();

        dc.asyncPostSignMatch(email,pss);

    }

    /**
     * Method to return to competitionselection after the game is signed.
     *
     * @exception InterruptedException if method fails.
     */
    public void finishAdmin() throws InterruptedException {
        Intent intent = new Intent(this, CompetitionSelection.class);
        startActivity(intent);
    }

    /**
     * @see MatchControl#onArticleSelected(Boolean, int)
     */
    @Override
    public void onArticleSelected(Boolean homeTeam, int playerId) {
        dc.setSelectedPlayer(homeTeam, playerId);
    }

    /**
     * adjusted version of {@link MatchControl#goalMade(View)}
     */
    public void goalMade(View view){

        Player sp = dc.getSelectedPlayer();
        if(sp!=null){
            //add the goal to the current selected player in domaincontroller
            dc.addGoal();

            //Logging
            addToLog(sp, "G","Goal by " + dc.getSelectedPlayer().getFullName());
            clearSelectedPlayer();


            teamsHeaderFragment.updateHeader();


        }else {
            toast("Select a player first.");
        }
    }

    /**
     * adjusted version of {@link MatchControl#changePlayers(View)}
     */
    public void changePlayers(View view) {

        Player sp = dc.getSelectedPlayer();
        if(sp!=null){

            clearSelectedPlayer();

        }else {
            toast("Select a player first.");
        }
    }

    /**
     * adjusted version of {@link MatchControl#injurySustained(View)}
     */
    public void injurySustained(View view){

        Player sp = dc.getSelectedPlayer();
        if(sp!=null){

            dc.addInjury();

            addToLog(sp, "I",dc.getSelectedPlayer().getFullName() + " got injured");

            updateBackgroundPlayer(sp);

            clearSelectedPlayer();

        }else {
            toast("Select a player first.");
        }

    }

    /**
     * adjusted version of {@link MatchControl#faultU20(View)}
     */
    public void faultU20(View view){

        //FIRST CHECK IF PLAYER ALREADY HAS 20 sec FAULT if not ignore press of button or give message player allready punished

        //If players selected preform action
        if(dc.getSelectedPlayer()!=null){
            Player sp = dc.getSelectedPlayer();
                //add the fault to the current selected player in domaincontroller
                dc.addFaultU20();

                //update the background color
                updateBackgroundPlayer(sp);

                //Logging
                addToLog(sp, "U", "Fault U20 for " + sp.getFullName() + ".");

                clearSelectedPlayer();      //clear selected player from layout

        }else {
            toast("Select a player first.");
        }

    }

    /**
     * adjusted version of {@link MatchControl#faultUMV(View)}
     */
    public void faultUMV(View view) {

        Player sp = dc.getSelectedPlayer();
        if(sp!=null){

            dc.addFaultUMV();

            addToLog(sp, "UMV","Fault UMV for " + sp.getFullName() + ".");

            updateBackgroundPlayer(sp);

            clearSelectedPlayer();

        }else {
            toast("Select a player first.");

        }
    }

    /**
     * adjusted version of {@link MatchControl#faultUMV4(View)}
     */
    public void faultUMV4(View view) {

        Player sp = dc.getSelectedPlayer();
        if(sp!=null){

            dc.addFaultUMV4();

            addToLog(sp, "UMV4","Fault UMV4 for " + sp.getFullName() + ".");
            updateBackgroundPlayer(sp);

            clearSelectedPlayer();

        }else {
            toast("Select a player first.");
        }

    }

    /**
     * adjusted version of {@link MatchControl#addToLog(Player, String, String)}
     * the roundtime is set to "END" to indicate changes happened after the official match time
     */
    public void addToLog(Player sp, String event, String description){
        //Determine home or away team
        String t;
        if(sp.getTeam().equals(dc.getHomeTeam())){
            t = "H";
        } else {
            t = "A";
        }

        dc.appendLog(description,event + t + dc.getMatch().getCurrentRound(), "END",dc.getMatch().getCurrentRound());

        updateLog();
    }

    /**
     * Method to reload the log for the fourth quarter.
     */
    private void updateLog() {
        q4 = dc.getLogForRound(4);
        lvActivitiesQ4.setAdapter(new ArrayAdapter<String>(AdministrationEnd.this,
                android.R.layout.simple_list_item_1, q4));
    }

    /**
     * {@link MatchControl#updateBackgroundPlayer(Player)}
     */
    private void updateBackgroundPlayer(Player sp){
        if(sp.getTeam().equals(dc.getHomeTeam())){
            homeTeam.updateBackgroundPlayer();
        } else {
            awayTeam.updateBackgroundPlayer();
        }
    }

    /**
     * {@link MatchControl#clearSelectedPlayer()}
     */
    public void clearSelectedPlayer(){
        homeTeam.resetFontPlayers();
        awayTeam.resetFontPlayers();
        dc.resetSelectedPlayer();
    }

    /**
     * {@link MatchControl#toast(String)}
     */
    public void toast(final String message){
           Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
           toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
           toast.show();
    }

    /**
     * Method to set the font of the sign icon.
     */
    private void setIconFont(TextView icon) {
            icon.setTypeface(FontManager.getTypeface(this, FontManager.FONTAWESOME));
    }



}
