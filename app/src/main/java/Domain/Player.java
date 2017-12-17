package Domain;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.Log;

import persistency.PlayerRest;

/**
 * Class that represents the players of a waterpolo team
 */
public class Player implements Comparable<Player>{

    //variables
    private int player_id;
    private int playerNumber;
    private String firstName;
    private String lastName;
    private Status status;
    private boolean starter;
    private Team team;
    private String photo;

    //FaultTimer
    private CountDownTimer faultTimer;
    private long faultTimeRemaining = 20000;

    public Player(int n, String lastName, String firstName){
        this.firstName = firstName;
        this.lastName = lastName;
        this.playerNumber = n;
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
        return firstName.charAt(0) + ". " + lastName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Method that uses an int value to set the correct status to the player
     *
     * @param statusNr 1 is active, 2 is suspended, 3 is game over, 4 is injured
     */
    public void setStatus(int statusNr){
        switch (statusNr){
            case 1:
                setStatus(Status.ACTIVE);
                break;
            case 2:
                setStatus(Status.SUSPENDED);
                break;
            case 3:
                setStatus(Status.GAMEOVER);
                break;
            case 4:
                setStatus(Status.INJURED);
                break;
        }
    }

    public int getPlayer_id() {
        return player_id;
    }
    public boolean getStarter(){
        return starter;
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

    public CountDownTimer getFaultTimer() {
        return faultTimer;
    }

    /**
     * Method that adds a 20 second penalty timer to the player when he commits a U20 fault
     */
    public void setFaultTimer() {
        if(faultTimer==null || faultTimeRemaining == 0){
            faultTimer = new CountDownTimer(20000,1000){

                @Override
                public void onTick(long millisUntilFinished) {
                    setFaultTimeRemaining(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    Log.d("FaultTest","FaultTimer for " + firstName + " has ended");
                    setFaultTimeRemaining(0);
                    faultTimer=null;
                }
            };
        } else {
            faultTimer.cancel();
            faultTimer = new CountDownTimer(getFaultTimeRemaining(), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    setFaultTimeRemaining(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    Log.d("FaultTest","FaultTimer for " + firstName + " has ended");
                    setFaultTimeRemaining(0);
                    faultTimer=null;
                }
            };
        }
    }

    public long getFaultTimeRemaining() {
        return faultTimeRemaining;
    }

    public void setFaultTimeRemaining(long faultTimeRemaining) {
        this.faultTimeRemaining = faultTimeRemaining;
    }

    @Override
    public int compareTo(@NonNull Player p) {
        if(playerNumber > p.getPlayerNumber()){
            return 1;
        } else if(playerNumber < p.getPlayerNumber()){
            return -1;
        } else{
            return 0;
        }
    }
}
