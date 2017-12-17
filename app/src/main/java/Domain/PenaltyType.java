package Domain;

/**
 * Enum class representing the different types of penalties that can be committed during a waterpolo match
 * The types are: U20: a standard fault, UMV: a heavy fault, UMV4: a brutality
 */
public enum PenaltyType {

    U20("U20",1),
    UMV("UMV",3),
    UMV4("UMV4", 3);

    private final int weight;
    private final String typeName;

    PenaltyType(String typeName, int weight) {
        this.typeName = typeName;
        this.weight = weight;
    }

    /**
     * The number of strikes a player gets for a certain fault, with a maximum of 3 for a game over.
     */
    public int getWeight(){
        return weight;
    }
}
