package Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Impling on 18-Oct-17.
 */

public class PenaltyBook {

    //Variables
    private int penaltyBook_id;
    private List<Penalty> penalties;

    //Constructor
    public PenaltyBook(){
        penalties = new ArrayList<Penalty>();
    }

    public void addPenalty(Penalty p){
        penalties.add(p);
    }

    public int getPenaltyWeightsForPlayer(int playerId){
        int weight = 0;

        for(Penalty p: penalties){
            if(p.getPlayer().getPlayer_id() == playerId){
                weight += p.getPenaltyType().getWeight();
            }
        }
        return weight;
    }

}
