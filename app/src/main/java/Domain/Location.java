package Domain;

/**
 * Created by Impling on 18-Oct-17.
 */

public class Location {

    //Variables
    private int location_id;
    private String locationName;
    private String Adress; //Maybe replace with differnt variables like country, city, postalcode, street and just create a fucntion to return the full address, could make it easier to filter matches

    public Location(String locationName, String adress) {
        this.locationName = locationName;
        Adress = adress;
    }
}
