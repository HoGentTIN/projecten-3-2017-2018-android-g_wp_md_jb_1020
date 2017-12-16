package Domain;

/**
 * Class to represent a scored goal.
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
