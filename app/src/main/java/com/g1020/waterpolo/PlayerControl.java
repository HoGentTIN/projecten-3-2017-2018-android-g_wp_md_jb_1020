package com.g1020.waterpolo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PlayerControl extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_control);
    /*
        //TEMPORARY CODE TO SHOWCASE CHRONO FUNCTIONALITY START
        //get intent that started the activity
        Intent intent = getIntent();
        String message = intent.getStringExtra(MatchControl.EXTRA_MESSAGE);

        //Capture layout textview and fill in the obtained string
        TextView textview = (TextView) findViewById(R.id.textView); //R.id.textview = find the component on screen R using it's id with value textField
        textview.setText(message);
        //TEMPORARY CODE TO SHOWCASE CHRONO FUNCTIONALITY END
    */
        PlayersHomeTeamFragment homeTeam = new PlayersHomeTeamFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.playercontainer, homeTeam).commit();

    }
}
