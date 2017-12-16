package Domain;


/**
 * Created by Impling on 18-Oct-17.
 */

public class Division {

    private int division_id;
    private String divisionName;
    private long roundLength;
    private long pauseLength;

    public Division(String divisionName,long roundLength, long pauseLength){
        this.divisionName = divisionName;
        this.roundLength = roundLength;
        this.pauseLength = pauseLength;
    }

    public String getDivisionName(){
        return divisionName;
    }

    public long getRoundLength() {
        //return 1; //for testing
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
