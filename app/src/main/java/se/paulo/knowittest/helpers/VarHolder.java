package se.paulo.knowittest.helpers;

import java.util.ArrayList;
import java.util.List;

import se.paulo.knowittest.models.Artist;

/** * Created by Paulo Vila Nova on 2017-08-07.
 */

public class VarHolder {

    private List<Artist> artistList = new ArrayList<>();


    private static VarHolder ourInstance = null;

    public static VarHolder getInstance(){
        if(ourInstance == null){
            ourInstance = new VarHolder();
        }
        return ourInstance;
    }


    public List<Artist> getArtistList() {
        return artistList;
    }

    public void setArtistList(List<Artist> artistList) {
        this.artistList = artistList;
    }
}
