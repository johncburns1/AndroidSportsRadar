package hu.ait.android.sportsradar.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnc on 12/11/2017.
 */

public class Match implements Parcelable{

    private String sportName;
    private String sportID;
    private String startTime;

    private List<Competitor> competitors;
    private String league;
    private String stadium;
    private String cityOfPlay;
    private String countryOfPlay;
    private List<Market> markets;
    private int icon;
    private String playType;

    public Match(String sportName, String sportID, String startTime, List<Competitor> competitors, String league, String stadium, String cityOfPlay, String countryOfPlay, List<Market> markets, int icon, String playType) {
        this.sportName = sportName;
        this.sportID = sportID;
        this.startTime = startTime;
        this.competitors = competitors;
        this.league = league;
        this.stadium = stadium;
        this.cityOfPlay = cityOfPlay;
        this.countryOfPlay = countryOfPlay;
        this.markets = markets;
        this.icon = icon;
        this.playType = playType;
    }

    public Match() {

    }

    protected Match(Parcel in) {
        sportName = in.readString();
        sportID = in.readString();
        startTime = in.readString();
        league = in.readString();
        stadium = in.readString();
        cityOfPlay = in.readString();
        countryOfPlay = in.readString();

        if(markets == null) {
            markets = new ArrayList<Market>();
        }

        in.readTypedList(markets, Market.CREATOR);

        if(competitors == null) {
            competitors = new ArrayList<Competitor>();
        }

        in.readTypedList(competitors, Competitor.CREATOR);
        icon = in.readInt();
        playType = in.readString();
    }

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getSportID() {
        return sportID;
    }

    public void setSportID(String sportID) {
        this.sportID = sportID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public List<Competitor> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(List<Competitor> competitors) {
        this.competitors = competitors;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getCityOfPlay() {
        return cityOfPlay;
    }

    public void setCityOfPlay(String cityOfPlay) {
        this.cityOfPlay = cityOfPlay;
    }

    public String getCountryOfPlay() {
        return countryOfPlay;
    }

    public void setCountryOfPlay(String countryOfPlay) {
        this.countryOfPlay = countryOfPlay;
    }

    public List<Market> getMarkets() {
        return markets;
    }

    public void setMarkets(List<Market> markets) {
        this.markets = markets;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getPlayType() {
        return playType;
    }

    public void setPlayType(String playType) {
        this.playType = playType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sportName);
        parcel.writeString(sportID);
        parcel.writeString(startTime);
        parcel.writeString(league);
        parcel.writeString(stadium);
        parcel.writeString(cityOfPlay);
        parcel.writeString(countryOfPlay);
        parcel.writeTypedList(markets);
        parcel.writeTypedList(competitors);
        parcel.writeInt(icon);
        parcel.writeString(playType);

    }
}
