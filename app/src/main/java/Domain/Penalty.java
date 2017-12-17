package Domain;

/**
 * Class that represents a fault that a player can commit
 */
public class Penalty {

    //Variables
    private int penalty_id;
    private Player player;
    private PenaltyType penaltyType;

    /**
     * Constructor to create penalties
     *
     * @param p player who has committed the penalty
     * @param pt the type of penalty committed. To check the types see {@link PenaltyType}
     */
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
