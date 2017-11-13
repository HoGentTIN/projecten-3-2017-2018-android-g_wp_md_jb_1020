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
}
