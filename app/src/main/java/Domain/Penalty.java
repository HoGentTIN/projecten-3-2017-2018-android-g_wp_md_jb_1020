package Domain;

/**
 * Created by Impling on 18-Oct-17.
 */

public class Penalty {

    //Variables
    private int penalty_id;
    private int Weight;

    private PenaltyType penaltyType;

    //Constructor
    public Penalty(int penalty_id, int weight) {
        this.penalty_id = penalty_id;
        this.Weight = weight;
    }
}
