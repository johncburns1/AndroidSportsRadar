package hu.ait.android.sportsradar.data;

/**
 * Created by johnc on 12/11/2017.
 */

public class ListSport {

    private String name;
    private SportResult response;
    private String sportID;
    private int image;

    public ListSport(String name, String sportID) {
        this.name = name;
        this.sportID = sportID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SportResult getResponse() {
        return response;
    }

    public void setResponse(SportResult response) {
        this.response = response;
    }

    public String getSportID() {
        return sportID;
    }

    public void setSportID(String sportID) {
        this.sportID = sportID;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
