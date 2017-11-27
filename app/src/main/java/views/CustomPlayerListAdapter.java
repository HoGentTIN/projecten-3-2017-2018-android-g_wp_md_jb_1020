package views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.g1020.waterpolo.PlayersFragment;
import com.g1020.waterpolo.R;

import java.util.List;

import application.ApplicationRuntime;
import Domain.Domaincontroller;
import Domain.Player;

/**
 * Created by pieter on 29/10/2017.
 */

public class CustomPlayerListAdapter extends ArrayAdapter<Player> {
        private ApplicationRuntime ar = ApplicationRuntime.getInstance();  //this adds temporary code to this class
        private Domaincontroller dc = ar.getDc();

        private PlayerHolder playerHolder;

    //to use colors in resources
    private int res[] = {R.color.btnColorU20,R.color.btnColorU20Pressed,R.color.btnColorUMV4Pressed};

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
                playerHolder = new PlayerHolder(v);
                v.setTag(playerHolder);

            }
            else {
                playerHolder = (PlayerHolder) v.getTag();
            }
            Player p = getItem(position);

            if (p != null) {

                if (playerHolder.txtPlayername != null) {
                    playerHolder.txtPlayername.setText(p.getFullName());
                }
                if (playerHolder.playerNumberTxtV != null){
                    int pNumber = p.getPlayerNumber();

                    // in comments to reduce laptop fan
                    setTeamColors(v,p);

                    StringBuilder sbPlayerNumber = new StringBuilder();
                    sbPlayerNumber.append(pNumber);
                    playerHolder.playerNumberTxtV.setText((sbPlayerNumber.toString()));
                }
            }

            return v;
        }

        private void setTeamColors(View v, Player p){

            v.setBackgroundResource(R.drawable.player_tile);

            if(p.getTeam().equals(dc.getMatch().getHomeTeam())) {
                playerHolder.playerNumberTxtV.setBackgroundResource(R.drawable.playernumber_home);
            }
            else{
                playerHolder.playerNumberTxtV.setStroke(1,Color.BLACK, Paint.Join.ROUND,1);
                playerHolder.playerNumberTxtV.setTextColor(Color.WHITE);
                playerHolder.playerNumberTxtV.setBackgroundResource(R.drawable.playernumber_away);
            }
            switch (p.getPlayerNumber()) {
                case 1:
                    playerHolder.playerNumberTxtV.setBackgroundResource(R.drawable.playernumber_goaly);
                    break;
                case 13:
                    playerHolder.playerNumberTxtV.setBackgroundResource(R.drawable.playernumber_goaly);
                    break;
                default:
                    break;
            }
        }


        //Function to set the name style of a list item
        public void setSelectedPlayer(int position, View convertView, int drawableId){
            View v = convertView;
            v.setBackgroundResource(drawableId);
        }

    public void updateBackgroundColors(int position, View v){
        Player p = getItem(position);
        //number of fault a player has
        int playerweight = dc.getMatch().getPenaltyBook().getPenaltyWeightsForPlayer(p.getPlayer_id());

        playerHolder.txtFaultField = (TextView) v.findViewById(R.id.txtFaultField);

        if(playerweight > 0) {
            if (playerweight > 3) {
                playerHolder.txtFaultField.setBackgroundResource(res[2]);
            } else {
                playerHolder.txtFaultField.setBackgroundResource(res[playerweight - 1]);
            }
        }

        //entire cell backgroundcolor
        //v.setBackgroundResource(res[p.getFaults()]);
    }

}
