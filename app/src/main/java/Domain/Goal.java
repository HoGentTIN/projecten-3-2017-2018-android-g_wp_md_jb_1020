package Domain;

/**
 * Created by pieter on 09/11/2017.
 */

public class Goal {

    private int playerid;
    private int matchid;

    private int teamid;

    private Player p;

    public Goal(int matchid, int teamid, int playerid){
        this.teamid = teamid;
        this.matchid = matchid;
        this.p = p;
        this.playerid = playerid;
    }

    public int getTeamid() {
        return teamid;
    }

}
