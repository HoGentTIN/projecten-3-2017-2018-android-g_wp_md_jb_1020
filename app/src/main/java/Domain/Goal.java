package Domain;

/**
 * Created by pieter on 09/11/2017.
 */

public class Goal {

    private Player p;

    public Goal(Player p){
        this.p = p;
    }

    public Player getPlayer() {
        return p;
    }

    public void setP(Player p) {
        this.p = p;
    }
}
