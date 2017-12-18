package Domain;

/**
 * Class to indicate the division where the team is currently playing
 */
public class Division {

    private int division_id;
    private String divisionName;
    private long roundLength;
    private long pauseLength;

    /**
     * Constructor to create the division
     *
     * @param divisionName name of the division
     * @param roundLength the length of a round for this division
     * @param pauseLength the length of a pause for this division
     */
    public Division(String divisionName,long roundLength, long pauseLength){
        this.divisionName = divisionName;
        this.roundLength = roundLength;
        this.pauseLength = pauseLength;
    }

    public String getDivisionName(){
        return divisionName;
    }

    public long getRoundLength() {
        //return 2; //for testing
        return roundLength;
    }
    public String getRoundLengthS() {
        return roundLength + ":00";
    }

    public void setRoundLength(long roundLength) {
        this.roundLength = roundLength;
    }

    public long getPauseLength() {
        return pauseLength;
    }
    public String getPauseLengthS() {
        return pauseLength + ":00";
    }


    public void setPauseLength(long pauseLength) {
        this.pauseLength = pauseLength;
    }
}
