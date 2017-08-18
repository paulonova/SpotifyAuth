package se.paulo.knowittest.models;

import android.os.Parcel;
import android.os.Parcelable;

/** * Created by Paulo Vila Nova on 2017-08-03.
 */

public class Artist implements Parcelable {

    private String name;
    private int popularity;
    private String genres;
    private String type;
    private String spotifyUrl;
    private int height;
    private int totalFlowers;
    private String imageUrl;

    public Artist() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTotalFlowers() {
        return totalFlowers;
    }

    public void setTotalFlowers(int totalFlowers) {
        this.totalFlowers = totalFlowers;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    protected Artist(Parcel in) {
        name = in.readString();
        popularity = in.readInt();
        genres = in.readString();
        type = in.readString();
        spotifyUrl = in.readString();
        height = in.readInt();
        totalFlowers = in.readInt();
        imageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(popularity);
        dest.writeString(genres);
        dest.writeString(type);
        dest.writeString(spotifyUrl);
        dest.writeInt(height);
        dest.writeInt(totalFlowers);
        dest.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}
