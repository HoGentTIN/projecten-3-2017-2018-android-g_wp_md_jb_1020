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
public class PlayersHomeTeamFragment extends Fragment {


    public PlayersHomeTeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_players_team, container, false);

        String[] players = {"Player1", "Player2", "Player3", "Player4"};
        String[] playersBench = {"BenchPlayer1", "BenchPlayer2", "BenchPlayer3"};

        ListView playersHomeTeam = (ListView) view.findViewById(R.id.hometeamplayers);
        ListView playersBenchHomeTeam = (ListView) view.findViewById(R.id.hometeambenchplayers);

        playersHomeTeam.setAdapter(createArrayAdapter(players));
        playersBenchHomeTeam.setAdapter(createArrayAdapter(playersBench));

        teamClickAction(playersHomeTeam);
        teamClickAction(playersBenchHomeTeam);

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
