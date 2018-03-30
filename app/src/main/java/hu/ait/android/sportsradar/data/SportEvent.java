package hu.ait.android.sportsradar.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnc on 12/11/2017.
 */

public class SportEvent implements Parcelable{

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("scheduled")
    @Expose
    public String scheduled;
    @SerializedName("start_time_tbd")
    @Expose
    public Boolean startTimeTbd;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("tournament_round")
    @Expose
    public TournamentRound tournamentRound;
    @SerializedName("season")
    @Expose
    public Season season;
    @SerializedName("tournament")
    @Expose
    public Tournament tournament;
    @SerializedName("competitors")
    @Expose
    public List<Competitor> competitors = null;
    @SerializedName("markets")
    @Expose
    public List<Market> markets = null;
    @SerializedName("markets_last_updated")
    @Expose
    public String marketsLastUpdated;
    @SerializedName("venue")
    @Expose
    public Venue venue;

    protected SportEvent(Parcel in) {
        id = in.readString();
        scheduled = in.readString();
        byte tmpStartTimeTbd = in.readByte();
        startTimeTbd = tmpStartTimeTbd == 0 ? null : tmpStartTimeTbd == 1;
        status = in.readString();
        if(markets == null) {
            markets = new ArrayList<Market>();
        }
        in.readTypedList(markets, Market.CREATOR);

        marketsLastUpdated = in.readString();

        if(competitors == null) {
            competitors = new ArrayList<Competitor>();
        }
        in.readTypedList(competitors, Competitor.CREATOR);
    }

    public static final Creator<SportEvent> CREATOR = new Creator<SportEvent>() {
        @Override
        public SportEvent createFromParcel(Parcel in) {
            return new SportEvent(in);
        }

        @Override
        public SportEvent[] newArray(int size) {
            return new SportEvent[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScheduled() {
        return scheduled;
    }

    public void setScheduled(String scheduled) {
        this.scheduled = scheduled;
    }

    public Boolean getStartTimeTbd() {
        return startTimeTbd;
    }

    public void setStartTimeTbd(Boolean startTimeTbd) {
        this.startTimeTbd = startTimeTbd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TournamentRound getTournamentRound() {
        return tournamentRound;
    }

    public void setTournamentRound(TournamentRound tournamentRound) {
        this.tournamentRound = tournamentRound;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public List<Competitor> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(List<Competitor> competitors) {
        this.competitors = competitors;
    }

    public List<Market> getMarkets() {
        return markets;
    }

    public void setMarkets(List<Market> markets) {
        this.markets = markets;
    }

    public String getMarketsLastUpdated() {
        return marketsLastUpdated;
    }

    public void setMarketsLastUpdated(String marketsLastUpdated) {
        this.marketsLastUpdated = marketsLastUpdated;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(scheduled);
        parcel.writeByte((byte) (startTimeTbd == null ? 0 : startTimeTbd ? 1 : 2));
        parcel.writeString(status);
        parcel.writeTypedList(markets);
        parcel.writeString(marketsLastUpdated);
        parcel.writeTypedList(competitors);
    }
}
