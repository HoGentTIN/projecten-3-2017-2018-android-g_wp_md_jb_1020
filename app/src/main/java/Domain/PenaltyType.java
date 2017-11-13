package Domain;

/**
 * Created by Impling on 18-Oct-17.
 */

public enum PenaltyType {

    // Not sure about these weights
    U20("U20",1),
    UMV("UMV",3),
    UMV4("UMV4", 3);

    // I guess 'weight' is the number of strikes a player gets for a certain fault
    private final int weight;
    private final String typeName;

    private PenaltyType(String typeName, int weight) {
        this.typeName = typeName;
        this.weight = weight;
    }

    public int getWeight(){
        return weight;
    }
}
