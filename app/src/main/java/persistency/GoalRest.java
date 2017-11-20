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
    @SerializedName("player_id")
    private int playerId;


    public GoalRest(int matchId, int teamId, int player) {
        this.goalId = goalId;
        this.matchId = matchId;
        this.teamId = teamId;
        this.playerId = player;
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

    public int getPlayer() {
        return playerId;
    }

    public void setPlayer(int player) {
        this.playerId = player;
    }
}
