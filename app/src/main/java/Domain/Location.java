package Domain;

import com.google.gson.annotations.SerializedName;

/**
 * Class that represents the location of the match
 */

public class Location {

    //Variables
    private int location_id;
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
        return String.format("%s%n%s%n%s %s %s",name,street,postalCode,city,country);
    }
}
