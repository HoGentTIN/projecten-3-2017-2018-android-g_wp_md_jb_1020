package Domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Impling on 18-Oct-17.
 */

public class Location {

    //Variables
    private int location_id;
    //private String Adress; //Maybe replace with differnt variables like country, city, postalcode, street and just create a fucntion to return the full address, could make it easier to filter matches

    private String name;
    private String street;

    @SerializedName("postalcode")
    private String postalCode;
    private String city;
    private String country;

    public Location(String name, String street, String city, String postalCode, String country) {

        this.name = name;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    public String getFullAddress(){
        return String.format("%s%n%s%n%s %s%n%s",name,street,postalCode,city,country);
    }
}
