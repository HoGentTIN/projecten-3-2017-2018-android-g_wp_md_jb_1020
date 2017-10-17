package Domain;

import java.util.Date;

/**
 * Created by timos on 5-10-2017.
 */

class Match {

    //Variables
    private int matchId;
    private String matchLog;
    private Team[] teams;               //always size 2 (1 is hometeam, 2 is awayteam)
    private Official[] officials;       //first official is always main official
    private Date matchDate;
    private String location;
    private int[] matchScores;          //always size 2 (1 is hometeam, 2 is awayteam)
    private int[] roundScores;



}
