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
import Domain.Team;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayersFragment extends Fragment {

    //TEMPORARY
    Domaincontroller dc;
    private static PlayersFragment pf;
    //TEMPORARY

    public PlayersFragment() {
        // Required empty public constructor
        ApplicationRuntime ar = ApplicationRuntime.getInstance();
        dc = ar.getDc();
    }

    //NULPOINTEREXCEPTION, creating multiple fragments
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
        List<Player> activePlayers = team.getPlayersByStatus(Status.ACTIVE);
        List<Player> benchedPlayers = team.getPlayersByStatus(Status.BENCHED);

        ListView playersHomeTeam = (ListView) view.findViewById(R.id.lsvplayers);

        Log.i("game",dc.getMatch().getTeam(0).getPlayers().get(0).getFullName());

        CustomPlayerListAdapter customPlayerAdapter = new CustomPlayerListAdapter(getContext(),android.R.id.text1,activePlayers);
        CustomPlayerListAdapter customBenchPlayerAdapter = new CustomPlayerListAdapter(getContext(),android.R.id.text1, benchedPlayers);

        playersHomeTeam.setAdapter(customPlayerAdapter);

        teamClickAction(playersHomeTeam);

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
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                array
        );

        return listViewAdapter;
    }


}
