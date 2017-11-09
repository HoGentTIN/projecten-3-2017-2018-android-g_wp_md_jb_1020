package Domain;

/**
 * Created by timos on 5-10-2017.
 */

public class Player {

    //variables
    private int player_id;
    private int playerNumber;
    private String firstName;
    private String lastName;
    private int age;            //would this normally not be birthdate
    private Status status;
    private Team team;

    // 3 strikes and you are out
    private int faults;

    public Player(int n, String lastName, String firstName){
        this.firstName = firstName;
        this.lastName = lastName;
        this.playerNumber = n;
        this.status = status;

        //temporary
        player_id = playerNumber;
    }

    public int getFaults() {
        return faults;
    }

    public void setFaults(int faults) {
        this.faults = faults;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName(){
        return lastName + " " + firstName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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


}
