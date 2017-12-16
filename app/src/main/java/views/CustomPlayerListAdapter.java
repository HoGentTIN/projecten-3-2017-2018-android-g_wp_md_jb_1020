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
 * An {@link ArrayAdapter} subclass.
 * Adapter for playerobjects in {@link PlayersFragment} listviews
 *
 */
public class CustomPlayerListAdapter extends ArrayAdapter<Player> {
        private ApplicationRuntime ar = ApplicationRuntime.getInstance();
        private Domaincontroller dc = ar.getDc();

        private PlayerHolder playerHolder;
        private int listLayout;

        //colors to indicate number of faults for players
        private int res[] = {R.color.btnColorU20,R.color.btnColorU20Pressed,R.color.btnColorUMV4Pressed};

        /**
         * Method that creates a new instance of playerfragment
         *
         *@param listLay sets the resource for the playerlist items for the adapter.
         *               This is used to display variable player tile heights
         */
        public CustomPlayerListAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Player> players, int listLay) {
            super(context, textViewResourceId, players);
            listLayout = listLay;
        }

        /**
         * Gets a View that displays the data at the specified position in the data set.
         * Assigns a {@link PlayerHolder} object to the view.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            if (convertView == null) {
                LayoutInflater vi = LayoutInflater.from(getContext());
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
                    setTeamColors(convertView,p);
                    playerHolder.playerNumberTxtV.setText((String.valueOf(pNumber)));
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

    /**
     * Method to set the correct colors for the player his capnumber
     * Hometeam = white & black text, awayteam = blue & white text
     * Goaly hometeam = red & black text, goaly awayteam = red & white text
     *
     * @param v View object for player
     * @param p Player object for which data is displayed
     */
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

    /**
     * Sets the style for the player cell
     *
     * @param convertView view object for the player
     * @param drawableId id for the drawable to set as background for the view object
     */
    public void setPlayerTileStyle(View convertView, int drawableId){
            convertView.setBackgroundResource(drawableId);
    }

    /**
     * Method that updates the fault indicators of the player tiles
     * Changes the color of the playernumber to gray when the player his game is over
     *
     * @param p player object of the player
     * @param v view object for the player
     */
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
            playerHolder.playerNumberTxtV.setBackgroundResource(R.drawable.playernumber_gameover);
        }
    }
}
