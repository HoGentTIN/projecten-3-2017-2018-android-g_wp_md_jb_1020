package com.g1020.waterpolo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Application.ApplicationRuntime;
import Domain.Domaincontroller;
import Domain.Player;
import Domain.Status;
import views.CustomPlayerListAdapter;


public class PlayersMatchSettingsFragment extends Fragment {

    private ApplicationRuntime ar;  //this adds temporary code to this class
    private Domaincontroller dc;
    private CustomPlayerListAdapter playerAdapter1;
    private CustomPlayerListAdapter playerAdapter2;
    private ListView lvPlayers;
    private ListView lvPlayers2;
    private TextView playerTitle;


    // TODO: Rename and change types of parameters




    public PlayersMatchSettingsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_players, container, false);
        ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();


        //ga nu nog de dummydata uit match gebruiken omdat de adapter voor players nog op player ipv playerRest staat
        List<Player> players = new ArrayList<>();


        for (Player player : dc.getMatch().getHomeTeam().getPlayers()){
          if(player.getStarter()) {
              players.add(player);
          }
        }
        //playerTitle = (TextView) view.findViewById(R.id.playerTitle);
       // playerTitle.setText(String.format("<-players as starter players that are not starters ->"));
        playerAdapter1 = new CustomPlayerListAdapter(getContext(),android.R.id.text1, players);
        List<Player> players2 = new ArrayList<>();
        for (Player player : dc.getMatch().getHomeTeam().getPlayers()){
            if(!player.getStarter()) {
                players2.add(player);
            }
        }
        playerAdapter2= new CustomPlayerListAdapter(getContext(),android.R.id.text1,players2);




        lvPlayers = (ListView) view.findViewById(R.id.lsvplayers);
        lvPlayers.setAdapter(playerAdapter1);
        lvPlayers2 = (ListView) view.findViewById(R.id.lsvplayers2);
        lvPlayers2.setAdapter(playerAdapter2);
        return view;

    }













    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
