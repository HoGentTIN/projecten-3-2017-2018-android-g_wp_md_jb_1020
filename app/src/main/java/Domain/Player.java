package Domain;

/**
 * Created by timos on 5-10-2017.
 */

public class Player {

    //variables
    private int player_id;
    private int playerNumber;
    private String name;        // private String firstname; private String lastname;
    private int age;            //would this normally not be birthdate
    private Status status;


    public Player(int n, String name){
        this.name = name;
        this.playerNumber = n;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
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


}
