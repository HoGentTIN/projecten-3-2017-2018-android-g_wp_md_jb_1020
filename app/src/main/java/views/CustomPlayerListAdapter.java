package views;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.g1020.waterpolo.R;

import java.util.List;

import Application.ApplicationRuntime;
import Domain.Domaincontroller;
import Domain.Player;

/**
 * Created by pieter on 29/10/2017.
 */

public class CustomPlayerListAdapter extends ArrayAdapter<Player> {
        private ApplicationRuntime ar = ApplicationRuntime.getInstance();  //this adds temporary code to this class
        private Domaincontroller dc = ar.getDc();

        private TextView txtPlayernumber;
        private TextView txtPlayername;

        private ImageView imgFault1;
        private ImageView imgFault2;
        private ImageView imgFault3;

        public CustomPlayerListAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Player> players) {
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
                    txtPlayernumber = (TextView) v.findViewById(R.id.txtplayernumber);
                    txtPlayername = (TextView) v.findViewById(R.id.txtplayername);
                    imgFault1 = (ImageView) v.findViewById(R.id.fault1);
                    imgFault2 = (ImageView) v.findViewById(R.id.fault2);
                    imgFault3 = (ImageView) v.findViewById(R.id.fault3);

                    if (txtPlayername != null) {
                        txtPlayername.setText(p.getFullName());
                    }
                    if (txtPlayernumber != null){
                        int pNumber = p.getPlayerNumber();

                        setTeamColors(p);

                        StringBuilder sbPlayerNumber = new StringBuilder();
                        sbPlayerNumber.append(pNumber);
                        txtPlayernumber.setText((sbPlayerNumber.toString()));
                    }

                }

            }
            return v;
        }
        private void setTeamColors(Player p){
            if(p.getTeam().equals(dc.getMatch().getTeam(0))) {
                txtPlayernumber.setBackgroundColor(Color.WHITE);
                txtPlayernumber.setTextColor(Color.BLACK);
            }
            else{
                txtPlayernumber.setBackgroundColor(Color.BLUE);
                txtPlayernumber.setTextColor(Color.WHITE);
            }
            switch (p.getPlayerNumber()) {
                case 1:
                    txtPlayernumber.setBackgroundColor(Color.RED);
                    break;
                case 13:
                    txtPlayernumber.setBackgroundColor(Color.RED);
                    break;
                default:
                    break;
            }
        }
    }
