package com.g1020.waterpolo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PlayerControl extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_control);

        PlayersFragment homeTeam = new PlayersFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.playercontainer, homeTeam).commit();

        PlayersFragment awayTeam = new PlayersFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.playerawaycontainer, awayTeam).commit();

       // LiveActivitiesFragment activities = new LiveActivitiesFragment();
        //getSupportFragmentManager().beginTransaction().add(R.id.liveactivitiescontainer, activities).commit();

        TeamsHeaderFragment teamsHeader = new TeamsHeaderFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.teamsheadercontainer, teamsHeader).commit();

    }
}
