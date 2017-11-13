package persistency;

import com.google.gson.annotations.SerializedName;

/**
 * Created by timos on 13-11-2017.
 */

public class GoalRest {

    @SerializedName("id")
    private int goalId;
    @SerializedName("match_id")
    private int matchId;
    @SerializedName("team_id")
    private int teamId;
    @SerializedName("player")
    private PlayerRest player;


    public GoalRest(int goalId, int matchId, int teamId, PlayerRest player) {
        this.goalId = goalId;
        this.matchId = matchId;
        this.teamId = teamId;
        this.player = player;
    }

    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public PlayerRest getPlayer() {
        return player;
    }

    public void setPlayer(PlayerRest player) {
        this.player = player;
    }
}
