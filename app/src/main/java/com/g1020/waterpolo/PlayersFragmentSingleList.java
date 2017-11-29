package com.g1020.waterpolo;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Domain.Domaincontroller;
import Domain.Player;
import Domain.Team;
import application.ApplicationRuntime;
import views.CustomPlayerListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayersFragmentSingleList extends Fragment {

    //TEMPORARY
    Domaincontroller dc;
    private static PlayersFragmentSingleList pf;
    private CustomPlayerListAdapter playerAdapter1;
    List<Player> currentPlayers;
    Player selectedPlayer;
    private ListView lvPlayers;

    private View viewToReset;

    //Playerselection parameters
    private int previousPlayerPosition;

    private PlayersFragmentSingleList otherTeam;

    // interface object to pass data
    OnPlayerSelectedListener playerListener;

    TextView playerTitle;



    //TEMPORARY

    //interface for passing data to matchcontrol
    public interface OnPlayerSelectedListener{
        public void onArticleSelected(Boolean homeTeam, int playerId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            playerListener = (OnPlayerSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPlayerSelectedListener");
        }

    }

    public PlayersFragmentSingleList() {
        // Required empty public constructor
        ApplicationRuntime ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();
    }

    public static PlayersFragmentSingleList newInstance(int teamNumber) {
        pf = new PlayersFragmentSingleList();

        Bundle args = new Bundle();
        args.putInt("teamNumber",teamNumber);
        pf.setArguments(args);

        return pf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_players_single_list, container, false);


        Team team;
        if(getArguments().getInt("teamNumber")==0){
            team = dc.getMatch().getHomeTeam();
        }else {
            team = dc.getMatch().getAwayTeam();
        }

        lvPlayers = (ListView) view.findViewById(R.id.lsvplayers);

        Log.i("game",dc.getMatch().getHomeTeam().getPlayers().get(0).getFullName());

        teamClickAction(lvPlayers);

        //playerTitle = (TextView) view.findViewById(R.id.playerTitle);
        setListPlayers(team);

        return view;
    }

    private void teamClickAction(final ListView listview) {

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                resetFontPlayers();
                otherTeam.resetFontPlayers();

                // retrieve the selected player
                previousPlayerPosition = position;
                selectedPlayer = (Player) listview.getItemAtPosition(position);


                Log.i("game","Position in list: " + Integer.toString(position) + ", Player name: " + selectedPlayer.getFullName());

                Boolean team = selectedPlayer.getTeam().equals(dc.getHomeTeam());

                Toast toast = Toast.makeText(getActivity(), "You've selected player " + selectedPlayer.getFullName(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

                playerListener.onArticleSelected(team, selectedPlayer.getPlayer_id());

                dc.setSelectedPlayer(team, selectedPlayer.getPlayer_id());

                CustomPlayerListAdapter ca = (CustomPlayerListAdapter) lvPlayers.getAdapter();

                Log.i("game", Integer.toString(listview.getChildCount()));
                Log.i("game", Integer.toString(selectedPlayer.getPlayerNumber()-1));

                ca.setSelectedPlayer(view,R.drawable.player_tile_selected );
                viewToReset = view;


            }
        });
    }

    //look for the place of the player in currentplayer list to determine in which list he's in
    private int getCurrentPlayerPositionList(){
                int playerListNumber = 0;
                for(Player cp: currentPlayers){
                    playerListNumber++;
                    if(cp.equals(selectedPlayer)){
                        Log.i("game", selectedPlayer.getFullName() + "has number in list: " + String.valueOf(playerListNumber));
                        return playerListNumber;
                    }
                }
                return 0;
    }

    //function to reset the visibility of currently selected player
    public void resetFontPlayers(){
        if(selectedPlayer!=null){
                CustomPlayerListAdapter ca = (CustomPlayerListAdapter) lvPlayers.getAdapter();
                ca.setSelectedPlayer(viewToReset, R.drawable.player_tile);
        }
    }

    public void updateBackgroundPlayer(){
        if(selectedPlayer != null) {
                CustomPlayerListAdapter ca = (CustomPlayerListAdapter) lvPlayers.getAdapter();
                ca.updateBackgroundColors(selectedPlayer.getPlayer_id(), viewToReset);
        }
    }

    public void updateBackgroundPlayers() {
        CustomPlayerListAdapter ca = (CustomPlayerListAdapter) lvPlayers.getAdapter();
        for(int i = 0; i < currentPlayers.size(); i++){
            ca.updateBackgroundColors(i, lvPlayers.getChildAt(i));
        }
    }

    public void setOtherTeam(PlayersFragmentSingleList opponent){
        otherTeam = opponent;
    }

    public void setListPlayers(Team team){
        currentPlayers = team.getPlayers();
        playerAdapter1 = new CustomPlayerListAdapter(getContext(),android.R.id.text1, currentPlayers.subList(0,13));

        lvPlayers.setAdapter(playerAdapter1);

    }
}
