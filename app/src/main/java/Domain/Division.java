package Domain;

import java.sql.Time;

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

}
