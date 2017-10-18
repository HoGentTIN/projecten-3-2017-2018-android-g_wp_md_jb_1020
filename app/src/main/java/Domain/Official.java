package Domain;

/**
 * Created by timos on 5-10-2017.
 */

//look into main-offical and sub-official differences before making this one
class Official {

    //Variables
    private int official_id;
    private String name;
    private String title;

    public Official(int official_id, String name, String title) {
        this.official_id = official_id;
        this.name = name;
        this.title = title;
    }
}
