package se.paulo.knowittest.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/** * Created by Paulo Vila Nova on 2017-07-13.
 */

public class HelperClass {

    private static String TOKEN_PREFERENCE = "token_preference";
    private static String TOKEN_KEY = "token_key";

    public static void saveSpotifyToken(Context context, String token){
        SharedPreferences sharedPreferences = context.getSharedPreferences(TOKEN_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();

        edit.putString(TOKEN_KEY, token);
        edit.commit();
    }


    public static String getSpotifyToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(TOKEN_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN_KEY, "");
    }

}
