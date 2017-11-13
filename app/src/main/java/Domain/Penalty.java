package Domain;

/**
 * Created by Impling on 18-Oct-17.
 */

public class Penalty {

    //Variables
    private int penalty_id;


    private Player player;
    private PenaltyType penaltyType;

    //Constructor
    public Penalty(Player p, PenaltyType pt) {
        this.player = p;
        this.penaltyType = pt;
    }

    public PenaltyType getPenaltyType(){
        return penaltyType;
    }

    public Player getPlayer() {
        return player;
    }
}
