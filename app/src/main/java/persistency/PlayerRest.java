package persistency;

import com.google.gson.annotations.SerializedName;

/**
 * Created by timos on 13-11-2017.
 */

public class PlayerRest {

    @SerializedName("id")
    private int playerId;
    @SerializedName("team_id")
    private int teamId;
    @SerializedName("player_number")
    private int playerNumber;
    @SerializedName("name")
    private String name;
    @SerializedName("Birtdate")
    private String birthDate;
    @SerializedName("status")
    private int status;
    @SerializedName("starter")
    private int starter;

    public PlayerRest(int playerId, int teamId, int playerNumber, String name, String birthDate, int status, int starter) {
        this.playerId = playerId;
        this.teamId = teamId;
        this.playerNumber = playerNumber;
        this.name = name;
        this.birthDate = birthDate;
        this.status = status;
        this.starter = starter;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStarter() {
        return starter;
    }

    public void setStarter(int starter) {
        this.starter = starter;
    }
}
