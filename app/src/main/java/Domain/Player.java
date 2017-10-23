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



}
