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
import android.widget.Toast;

import java.util.List;

import Domain.Domaincontroller;
import Domain.Player;
import Domain.Team;
import application.ApplicationRuntime;
import views.CustomPlayerListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * This fragment is used to display the players of a team, displayed in a single list
 * @see PlayersFragment for the used methods
 */

public class PlayersFragmentSingleList extends Fragment {

    private Domaincontroller dc;
    private Player selectedPlayer;
    private ListView lvPlayers;
    private View viewToReset;

    private PlayersFragmentSingleList otherTeam;

    // interface object to pass data
    private OnPlayerSelectedListener playerListener;


    //interface for passing data to matchcontrol
    public interface OnPlayerSelectedListener{
        void onArticleSelected(Boolean homeTeam, int playerId);
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
        ApplicationRuntime ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();
    }

    public static PlayersFragmentSingleList newInstance(int teamNumber) {
        PlayersFragmentSingleList pf = new PlayersFragmentSingleList();

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

                ca.setPlayerTileStyle(view,R.drawable.player_tile_selected );
                viewToReset = view;


            }
        });
    }

    //function to reset the visibility of currently selected player
    public void resetFontPlayers(){
        if(selectedPlayer!=null){
                CustomPlayerListAdapter ca = (CustomPlayerListAdapter) lvPlayers.getAdapter();
                ca.setPlayerTileStyle(viewToReset, R.drawable.player_tile);
        }
    }

    public void updateBackgroundPlayer(){
        if(selectedPlayer != null) {
                CustomPlayerListAdapter ca = (CustomPlayerListAdapter) lvPlayers.getAdapter();
                ca.updateBackgroundColors(selectedPlayer, viewToReset);
        }
    }

    public void setOtherTeam(PlayersFragmentSingleList opponent){
        otherTeam = opponent;
    }

    public void setListPlayers(Team team){
        List<Player> currentPlayers = team.getPlayers();
        CustomPlayerListAdapter playerAdapter1 = new CustomPlayerListAdapter(getContext(), android.R.id.text1, currentPlayers.subList(0, 13), R.layout.list_item_players_administration);

        lvPlayers.setAdapter(playerAdapter1);

    }
}
