package com.g1020.waterpolo;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import Application.ApplicationRuntime;
import Domain.CompetitionClass;
import Domain.Domaincontroller;
import Domain.Player;
import Domain.Status;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayersFragment extends Fragment {

    //TEMPORARY
    ApplicationRuntime ar;  //this adds temporary code to this class
    Domaincontroller dc;
    //TEMPORARY

    public PlayersFragment() {
        // Required empty public constructor
    }

    /* NULPOINTEREXCEPTION, creating multiple fragments
    public static PlayersFragment newInstance(String[] players) {
        pf = new PlayersFragment();

        Bundle args = new Bundle();
        args.putStringArray("players",players);
        pf.setArguments(args);

        return pf;
    } */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_players, container, false);
        ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();

        //NULLPOINTER
        // currentPlayers = getArguments().getStringArray("players");


        ListView playersHomeTeam = (ListView) view.findViewById(R.id.lsvplayers);
        ListView playersBenchHomeTeam = (ListView) view.findViewById(R.id.lsvBenched);

        Log.i("game",dc.getMatch().getTeam(0).getPlayers().get(0).getFullName());

        CustomPlayerListAdapter customPlayerAdapter = new CustomPlayerListAdapter(getContext(),android.R.id.text1,dc.getMatch().getTeam(0).getPlayersByStatus(Status.ACTIVE));
        CustomPlayerListAdapter customBenchPlayerAdapter = new CustomPlayerListAdapter(getContext(),android.R.id.text1,dc.getMatch().getTeam(0).getPlayersByStatus(Status.BENCHED));

        playersHomeTeam.setAdapter(customPlayerAdapter);
        playersBenchHomeTeam.setAdapter(customBenchPlayerAdapter);

        teamClickAction(playersHomeTeam);
        teamClickAction(playersBenchHomeTeam);

        return view;
    }

    private void teamClickAction(ListView listview) {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "You've clicked item " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayAdapter createArrayAdapter(String[] array) {
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                array
        );

        return listViewAdapter;
    }


}
