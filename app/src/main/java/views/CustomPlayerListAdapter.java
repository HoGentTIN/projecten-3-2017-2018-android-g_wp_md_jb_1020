package views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.g1020.waterpolo.PlayersFragment;
import com.g1020.waterpolo.R;

import java.util.List;

import Domain.Status;
import application.ApplicationRuntime;
import Domain.Domaincontroller;
import Domain.Player;
import persistency.PlayerRest;

/**
 * Created by pieter on 29/10/2017.
 */

public class CustomPlayerListAdapter extends ArrayAdapter<Player> {
        private ApplicationRuntime ar = ApplicationRuntime.getInstance();  //this adds temporary code to this class
        private Domaincontroller dc = ar.getDc();

        private PlayerHolder playerHolder;
        private int listLayout;

    //to use colors in resources
    private int res[] = {R.color.btnColorU20,R.color.btnColorU20Pressed,R.color.btnColorUMV4Pressed};

        public CustomPlayerListAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Player> players, int listLay) {
            super(context, textViewResourceId, players);
            listLayout = listLay;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            if (convertView == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                convertView = vi.inflate(listLayout, null);
                playerHolder = new PlayerHolder(convertView);
                convertView.setTag(playerHolder);
            }
            else {
                playerHolder = (PlayerHolder) convertView.getTag();
            }

            Player p = getItem(position);

            if (p != null) {

                if (playerHolder.txtPlayername != null) {
                    playerHolder.txtPlayername.setText(p.getFullName());
                }
                if (playerHolder.playerNumberTxtV != null){
                    int pNumber = p.getPlayerNumber();

                    // in comments to reduce laptop fan
                    setTeamColors(convertView,p);

                    StringBuilder sbPlayerNumber = new StringBuilder();
                    sbPlayerNumber.append(pNumber);
                    playerHolder.playerNumberTxtV.setText((sbPlayerNumber.toString()));
                }
                //check if player already has faults, this way it's correctly displayed in administrationEnd
                updateBackgroundColors(p,convertView);
            }


            return convertView;
        }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }


    private void setTeamColors(View v, Player p){

            //assign a standerd player tile to the players
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
    public void setSelectedPlayer(View convertView, int drawableId){
            convertView.setBackgroundResource(drawableId);
    }

    public void updateBackgroundColors(Player p, View v){

        playerHolder = (PlayerHolder) v.getTag();

        //number of faults a player has
        int playerFaults = dc.getMatch().getPenaltyBook().getPenaltyWeightsForPlayer(p.getPlayer_id());

            if (dc.getMatch().getPenaltyBook().getPenaltyWeightsForPlayer(p.getPlayer_id()) > 0) {
                if (playerFaults > 3) {
                    playerHolder.txtFaultField.setBackgroundResource(res[2]);
                } else {
                    playerHolder.txtFaultField.setBackgroundResource(res[dc.getMatch().getPenaltyBook().getPenaltyWeightsForPlayer(p.getPlayer_id()) - 1]);
                }
            }

        if(p.getStatus() == Status.GAMEOVER){
                Log.i("game", "changing background color to game over");
            playerHolder.playerNumberTxtV.setBackgroundResource(R.drawable.playernumber_gameover);
        }
        //entire cell backgroundcolor
        //v.setBackgroundResource(res[p.getFaults()]);
    }
}
