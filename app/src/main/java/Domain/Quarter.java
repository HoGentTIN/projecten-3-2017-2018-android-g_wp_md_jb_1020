package Domain;

/**
 * Created by Impling on 18-Oct-17.
 */

public class Quarter {

    //Variables
    private int scoreHome = 0;
    private int scoreAway = 0;
    private int quarterPeriod = 0;

    public int getQuarterPeriod() {
        return quarterPeriod;
    }

    public void setQuarterPeriod(int quarterPeriod) {
        this.quarterPeriod = quarterPeriod;
    }

    public int getScoreHome() {
        return scoreHome;
    }

    public int getScoreAway() {
        return scoreAway;
    }

    public void addScoreHome(){
        scoreHome++;
    }

    public void addScoreAway(){
        scoreAway++;
    }
}
