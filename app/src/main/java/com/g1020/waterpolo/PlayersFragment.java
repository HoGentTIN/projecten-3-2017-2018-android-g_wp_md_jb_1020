package com.g1020.waterpolo;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Application.ApplicationRuntime;
import Domain.Domaincontroller;
import Domain.Player;
import Domain.Status;
import Domain.Team;
import views.CustomPlayerListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayersFragment extends Fragment {

    //TEMPORARY
    Domaincontroller dc;
    private static PlayersFragment pf;
    private CustomPlayerListAdapter playerAdapter1;
    private CustomPlayerListAdapter playerAdapter2;
    List<Player> currentPlayers;
    Player selectedPlayer;
    private ListView lvPlayers;
    private ListView lvPlayers2;

    //Playerselection parameters
    private int previousPlayerPosition;
    private PlayersFragment otherTeam;

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

    public PlayersFragment() {
        // Required empty public constructor
        ApplicationRuntime ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();
    }

    public static PlayersFragment newInstance(int teamNumber) {
        pf = new PlayersFragment();

        Bundle args = new Bundle();
        args.putInt("teamNumber",teamNumber);
        pf.setArguments(args);

        return pf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_players, container, false);

        Team team = dc.getMatch().getTeam(getArguments().getInt("teamNumber"));

        lvPlayers = (ListView) view.findViewById(R.id.lsvplayers);
        lvPlayers2 = (ListView) view.findViewById(R.id.lsvplayers2);

        Log.i("game",dc.getMatch().getTeam(0).getPlayers().get(0).getFullName());

        teamClickAction(lvPlayers);
        teamClickAction(lvPlayers2);

        playerTitle = (TextView) view.findViewById(R.id.playerTitle);
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
                Boolean team = selectedPlayer.getTeam().isHomeTeam();

                Toast toast = Toast.makeText(getActivity(), "You've selected player " + selectedPlayer.getFullName(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

                playerListener.onArticleSelected(team, selectedPlayer.getPlayer_id());


                //Change look of selected item
                if(selectedPlayer.getTeam().isHomeTeam()){
                    CustomPlayerListAdapter ca = (CustomPlayerListAdapter) lvPlayers.getAdapter();
                    ca.setSelectedPlayer(position,  listview.getChildAt(position), Typeface.BOLD );
                }else{
                    CustomPlayerListAdapter ca = (CustomPlayerListAdapter) lvPlayers2.getAdapter();
                    ca.setSelectedPlayer(position,  listview.getChildAt(position), Typeface.BOLD );
                }



            }
        });
    }

    //function to reset the visibility of currently selected player
    public void resetFontPlayers(){
        if(selectedPlayer!=null){
            //Change look of previous item
            CustomPlayerListAdapter ca = (CustomPlayerListAdapter) lvPlayers.getAdapter();
            ca.setSelectedPlayer(previousPlayerPosition,  lvPlayers.getChildAt(previousPlayerPosition), Typeface.NORMAL );
            ca = (CustomPlayerListAdapter) lvPlayers2.getAdapter();
            ca.setSelectedPlayer(previousPlayerPosition,  lvPlayers2.getChildAt(previousPlayerPosition), Typeface.NORMAL );

        }
    }

    public void setOtherTeam(PlayersFragment opponent){
        otherTeam = opponent;
    }

    public void setListPlayers(Team team){
        currentPlayers = team.getPlayers();
        playerTitle.setText(String.format(getResources().getString(R.string.playerFragmentTitle),currentPlayers.get(0).getStatus().toString()));

        playerAdapter1 = new CustomPlayerListAdapter(getContext(),android.R.id.text1, currentPlayers.subList(0,7));
        playerAdapter2 = new CustomPlayerListAdapter(getContext(),android.R.id.text1, currentPlayers.subList(7,currentPlayers.size()));

        lvPlayers.setAdapter(playerAdapter1);
        lvPlayers2.setAdapter(playerAdapter2);

    }


}
