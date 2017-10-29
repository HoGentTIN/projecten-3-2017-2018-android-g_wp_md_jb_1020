package com.g1020.waterpolo;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import Domain.CompetitionClass;
import Domain.Player;
import Domain.Team;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayersFragment extends Fragment {

    //TEMPORARY
    private Team team = new Team("Gent",CompetitionClass.U20);
    private String[] players = {"Player1", "Player2", "Player3", "Player4"};
    private Player[] playerObjects = {new Player(1,"Test"),new Player(2,"Test"),new Player(3,"Test"),
            new Player(7,"Test"),new Player(10,"Test"),new Player(5,"Test"),new Player(8,"Test")};
    //TEMPORARY

    public PlayersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_players, container, false);

        // TEMPORARY
        team.addPlayers(Arrays.asList(playerObjects));
        // TEMPORARY

        String[] playersBench = {"BenchPlayer1", "BenchPlayer2", "BenchPlayer3"};

        ListView playersHomeTeam = (ListView) view.findViewById(R.id.lsvplayers);
        ListView playersBenchHomeTeam = (ListView) view.findViewById(R.id.lsvBenched);

        CustomPlayersListAdapter customPlayerAdapter = new CustomPlayersListAdapter(getContext(),android.R.id.text1,team.getPlayers());

        playersHomeTeam.setAdapter(customPlayerAdapter);
        playersBenchHomeTeam.setAdapter(createArrayAdapter(playersBench));

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

    public class CustomPlayersListAdapter extends ArrayAdapter<Player> {

        public CustomPlayersListAdapter(@NonNull Context context,int textViewResourceId, @NonNull List<Player> players) {
            super(context, textViewResourceId, players);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.list_item_players_custom, null);

                Player p = getItem(position);

                if (p != null) {
                    TextView txtPlayernumber = (TextView) v.findViewById(R.id.txtplayernumber);
                    TextView txtPlayername = (TextView) v.findViewById(R.id.txtplayername);
                    ImageView imgFault1 = (ImageView) v.findViewById(R.id.fault1);
                    ImageView imgFault2 = (ImageView) v.findViewById(R.id.fault2);
                    ImageView imgFault3 = (ImageView) v.findViewById(R.id.fault3);

                    if (txtPlayername != null) {
                        txtPlayername.setText(p.getName());
                    }
                    if (txtPlayernumber != null){
                        int pNumber = p.getPlayerNumber();
                        switch (pNumber) {
                            case 1:
                                txtPlayernumber.setBackgroundColor(Color.RED);
                                break;
                            case 13:
                                txtPlayernumber.setBackgroundColor(Color.RED);
                                break;
                            default:
                                txtPlayernumber.setBackgroundColor(Color.WHITE);
                        }

                        StringBuilder sbPlayerNumber = new StringBuilder();
                        sbPlayerNumber.append(pNumber);
                        txtPlayernumber.setText((sbPlayerNumber.toString()));
                    }

                }

            }
            return v;
        }
    }
}
