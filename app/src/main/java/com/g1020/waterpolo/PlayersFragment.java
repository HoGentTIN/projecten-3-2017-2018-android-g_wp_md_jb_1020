package com.g1020.waterpolo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

    TextView playerTitle;
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

        lvPlayers = (ListView) view.findViewById(R.id.lsvplayers);
        lvPlayers2 = (ListView) view.findViewById(R.id.lsvplayers2);

        Log.i("game",dc.getMatch().getTeam(0).getPlayers().get(0).getFullName());

        teamClickAction(lvPlayers);

        playerTitle = (TextView) view.findViewById(R.id.playerTitle);
        setListPlayers(team);

        return view;
    }

    private void teamClickAction(final ListView listview) {

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // retrieve the selected player
                selectedPlayer = (Player) listview.getItemAtPosition(position);
                Toast.makeText(getActivity(), "You've selected player " + selectedPlayer.getFullName(), Toast.LENGTH_SHORT).show();
            }
        });
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
