package Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that holds all of the faults committed during a match.
 */
public class PenaltyBook {

    //Variables
    private int penaltyBook_id;
    private List<Penalty> penalties;

    public PenaltyBook(){
        penalties = new ArrayList<Penalty>();
    }

    public void addPenalty(Penalty p){
        penalties.add(p);
    }

    /**
     * Method that calculates the total amount of penalty weights a certain player has acquired during a single match
     *
     * @param playerId id for the player for which the weight needs to be calculated
     * @return total amount of weight for the faults the player has committed
     */
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
