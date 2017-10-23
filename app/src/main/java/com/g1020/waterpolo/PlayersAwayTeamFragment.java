package com.g1020.waterpolo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayersAwayTeamFragment extends Fragment {


    public PlayersAwayTeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_players_awayteam, container, false);

        String[] players = {"AwayPlayer1", "AwayPlayer2", "AwayPlayer3", "AwayPlayer4"};
        String[] playersBench = {"BenchPlayer1", "BenchPlayer2", "BenchPlayer3"};

        ListView playersAwayTeam = (ListView) view.findViewById(R.id.awayteamplayers);
        ListView playersBenchAwayTeam = (ListView) view.findViewById(R.id.awayteambenchplayers);

        playersAwayTeam.setAdapter(createArrayAdapter(players));
        playersBenchAwayTeam.setAdapter(createArrayAdapter(playersBench));

        teamClickAction(playersAwayTeam);
        teamClickAction(playersBenchAwayTeam);

        return view;
    }

    private ArrayAdapter createArrayAdapter(String[] array) {
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                array
        );

        return listViewAdapter;
    }

    private void teamClickAction(ListView listview) {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "You've clicked item " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
