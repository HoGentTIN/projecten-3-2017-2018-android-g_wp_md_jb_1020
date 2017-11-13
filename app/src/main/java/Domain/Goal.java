package Domain;

/**
 * Created by pieter on 09/11/2017.
 */

public class Goal {

    private int playerid;
    private int matchid;

    private int teamid;

    private Player p;

    public Goal(int matchid, int teamid, Player p){
        this.teamid = teamid;
        this.matchid = matchid;
        this.p = p;
        this.playerid = p.getPlayer_id();
    }

    public int getTeamid() {
        return teamid;
    }

}
