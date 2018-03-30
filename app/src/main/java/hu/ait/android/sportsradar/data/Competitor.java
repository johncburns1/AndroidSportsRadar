package hu.ait.android.sportsradar.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by johnc on 12/11/2017.
 */

public class Competitor implements Parcelable{

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("country_code")
    @Expose
    public String countryCode;
    @SerializedName("abbreviation")
    @Expose
    public String abbreviation;
    @SerializedName("qualifier")
    @Expose
    public String qualifier;
    @SerializedName("rotation_number")
    @Expose
    public Integer rotationNumber;

    protected Competitor(Parcel in) {
        id = in.readString();
        name = in.readString();
        country = in.readString();
        countryCode = in.readString();
        abbreviation = in.readString();
        qualifier = in.readString();
        if (in.readByte() == 0) {
            rotationNumber = null;
        } else {
            rotationNumber = in.readInt();
        }
    }

    public static final Creator<Competitor> CREATOR = new Creator<Competitor>() {
        @Override
        public Competitor createFromParcel(Parcel in) {
            return new Competitor(in);
        }

        @Override
        public Competitor[] newArray(int size) {
            return new Competitor[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public Integer getRotationNumber() {
        return rotationNumber;
    }

    public void setRotationNumber(Integer rotationNumber) {
        this.rotationNumber = rotationNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(country);
        parcel.writeString(countryCode);
        parcel.writeString(abbreviation);
        parcel.writeString(qualifier);
        if (rotationNumber == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(rotationNumber);
        }
    }
}
