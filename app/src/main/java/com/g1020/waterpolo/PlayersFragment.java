package com.g1020.waterpolo;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import application.ApplicationRuntime;
import Domain.Domaincontroller;
import Domain.Player;
import Domain.Team;
import views.CustomPlayerListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * This fragment is used to display the players of a team, divided in two lists
 */
public class PlayersFragment extends Fragment {

    private Domaincontroller dc;
    private PlayersFragment otherTeam;

    private List<Player> currentPlayers;
    private ListView lvPlayers;
    private ListView lvPlayers2;

    private Player selectedPlayer;
    private View viewToReset;

    // interface object to pass data
    private OnPlayerSelectedListener playerListener;


    /**
     * Interface class for passing data to matchcontrol
     *
     */
    public interface OnPlayerSelectedListener{
        /**
         * Method to pass objects from the fragment to the activity
         *
         * @param homeTeam indicates if the player his team is the home or awayteam
         * @param playerId the id of the selected player
         */
        void onArticleSelected(Boolean homeTeam, int playerId);
    }

    /**
     * Method to initialize the listener object
     *
     * @param activity fragment interface
     */
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

    /**
     * Default constructor.
     * Initializes the domaincontroller
     *
     */
    public PlayersFragment() {
        ApplicationRuntime ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();
    }

    /**
     * Static method that creates a new instance of playerfragment
     *
     *@param teamNumber can be "0" for hometeam or "1" for awayteam.
     */
    public static PlayersFragment newInstance(int teamNumber) {
        PlayersFragment pf = new PlayersFragment();

        Bundle args = new Bundle();
        args.putInt("teamNumber",teamNumber);
        pf.setArguments(args);

        return pf;
    }

    /**
     * Initializes the correct team depending on the teamnumber
     * @see #newInstance(int)
     *
     * adds players into the listviews
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_players, container, false);


        Team team;
        if(getArguments().getInt("teamNumber")==0){
            team = dc.getMatch().getHomeTeam();
        }else {
            team = dc.getMatch().getAwayTeam();
        }

        lvPlayers = (ListView) view.findViewById(R.id.lsvplayers);
        lvPlayers2 = (ListView) view.findViewById(R.id.lsvplayers2);

        teamClickAction(lvPlayers);
        teamClickAction(lvPlayers2);

        setListPlayers(team);

        return view;
    }

    /**
     * Method that sets the onclickListener for the player listviews.
     * Sets the selected player in the domaincontroller and updates the playertiles for selected and unselected players
     *
     * @param listview listview on which the onItemClickListener resides
     */
    private void teamClickAction(final ListView listview) {

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                resetFontPlayers();
                otherTeam.resetFontPlayers();

                // retrieve the selected player
                selectedPlayer = (Player) listview.getItemAtPosition(position);

                Boolean team = selectedPlayer.getTeam().equals(dc.getHomeTeam());

                Toast toast = Toast.makeText(getActivity(), "You've selected player " + selectedPlayer.getFullName(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

                playerListener.onArticleSelected(team, selectedPlayer.getPlayer_id());

                dc.setSelectedPlayer(team, selectedPlayer.getPlayer_id());

                if(getCurrentPlayerPositionList() < 8){
                    CustomPlayerListAdapter ca = (CustomPlayerListAdapter) lvPlayers.getAdapter();
                    ca.setPlayerTileStyle(view, R.drawable.player_tile_selected );
                }else{
                    CustomPlayerListAdapter ca = (CustomPlayerListAdapter) lvPlayers2.getAdapter();
                    ca.setPlayerTileStyle(view, R.drawable.player_tile_selected );
                }

                viewToReset = view;

            }
        });
    }

    //look for the place of the player in currentplayer list to determine in which list he's in
    /**
     * Method that looks for the selected players position in the list of current players.
     * This method is needed to access the correct listview of players to change the looks of the playertile
     *
     * @return the selected players position in the list
     */
    private int getCurrentPlayerPositionList(){
                int playerListNumber = 0;
                for(Player cp: currentPlayers){
                    playerListNumber++;
                    if(cp.equals(selectedPlayer)){
                        return playerListNumber;
                    }
                }
                return 0;
    }

    /**
     * Method that resets the layout of the previous selected player to a standard tile
     *
     */
    public void resetFontPlayers(){
        if(selectedPlayer!=null){
            if(getCurrentPlayerPositionList() < 8) {
                CustomPlayerListAdapter ca = (CustomPlayerListAdapter) lvPlayers.getAdapter();
                ca.setPlayerTileStyle(viewToReset, R.drawable.player_tile);
            } else {
                CustomPlayerListAdapter ca = (CustomPlayerListAdapter) lvPlayers2.getAdapter();
                ca.setPlayerTileStyle(viewToReset, R.drawable.player_tile);
            }
        }
    }

    /**
     * Method that looks for the correct adapter of the selected player to update the background
     *
     */
    public void updateBackgroundPlayer(){
        if(selectedPlayer != null) {
            if (getCurrentPlayerPositionList() < 8) {
                CustomPlayerListAdapter ca = (CustomPlayerListAdapter) lvPlayers.getAdapter();
                ca.updateBackgroundColors(selectedPlayer, viewToReset);
            } else {
                CustomPlayerListAdapter ca2 = (CustomPlayerListAdapter) lvPlayers2.getAdapter();
                ca2.updateBackgroundColors(selectedPlayer, viewToReset);
            }
        }
    }

    /**
     * Method that sets the fragment of the other team.
     *
     * @param opponent the fragment of the other team
     */
    public void setOtherTeam(PlayersFragment opponent){
        otherTeam = opponent;
    }

    /**
     * Method that loads the players of the correct team into the class.
     * Sets the adapters to the listviews
     *
     * @param team the team that is displayed in this fragment
     */
    public void setListPlayers(Team team){
        currentPlayers = team.getPlayers();

        CustomPlayerListAdapter playerAdapter1 = new CustomPlayerListAdapter(getContext(), android.R.id.text1, currentPlayers.subList(0, 7), R.layout.list_item_players_match_control);
        CustomPlayerListAdapter playerAdapter2 = new CustomPlayerListAdapter(getContext(), android.R.id.text1, currentPlayers.subList(7, 13), R.layout.list_item_players_match_control);
        lvPlayers.setAdapter(playerAdapter1);
        lvPlayers2.setAdapter(playerAdapter2);

    }
}
