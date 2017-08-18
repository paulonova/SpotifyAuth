package se.paulo.knowittest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import se.paulo.knowittest.adapter.ArtistSearchAdapter;
import se.paulo.knowittest.helpers.HelperClass;
import se.paulo.knowittest.helpers.VarHolder;
import se.paulo.knowittest.models.Artist;

public class MainActivity extends AppCompatActivity implements SpotifyPlayer.NotificationCallback, ConnectionStateCallback {

    @BindView(R.id.relativeLayoutSearchButton)  RelativeLayout relativeLayoutSearchButton;
    @BindView(R.id.edtSearchArtist)  EditText edtSearchArtist;
    @BindView(R.id.recyclerViewShowItems)   RecyclerView recyclerViewShowItems;

    private static final String CLIENT_ID = "b3fc39e5f63641e6904b5686f5463a50";
    private static final String REDIRECT_URI = "http://my_first_search/callback/";
    private static final String BASE_URL = "https://api.spotify.com/v1/search?q=";
    private static final String TYPE_URL = "&type=artist";

    private Artist artist;
    private ArrayList<String> genresList;
    private ArrayList<Artist> artistArrayList;

    private VarHolder varHolder = VarHolder.getInstance();


    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;
    public static final String TAG = MainActivity.class.getSimpleName();

    public List<Artist> artistList;
    private ArtistSearchAdapter artistSearchAdapter;

    private Player mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);

        artistList = new ArrayList<>();

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                                                    AuthenticationResponse.Type.TOKEN,
                                                    REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
        getArtistName(relativeLayoutSearchButton);

    }

    private void getArtistName(RelativeLayout relativeLayoutSearchButton){

        relativeLayoutSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = edtSearchArtist.getText().toString();
                Log.i(TAG, "Name: " + putNamesTogether(name));
                getSpotifyInformationApi(getApplicationContext(), putNamesTogether(name));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        artistSearchAdapter = new ArtistSearchAdapter(MainActivity.this, artistList);
                        recyclerViewShowItems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerViewShowItems.setAdapter(artistSearchAdapter);
                        artistSearchAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }

    private String putNamesTogether(String name){
        String []parts = name.split(" ");
        if(parts.length == 1){
            return name;
        }else if(parts.length >= 2){
            return parts[0] + "+" + parts[parts.length - 1];
        }

        return "";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);

                Log.d("TOKEN", "" + response.getAccessToken());
                HelperClass.saveSpotifyToken(this, response.getAccessToken());

                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {

                        try {
                            mPlayer = spotifyPlayer;
                            mPlayer.addConnectionStateCallback(MainActivity.this);
                            mPlayer.addNotificationCallback(MainActivity.this);
                        } catch (Exception e) {
                            Log.e(TAG, "ERROR: " + e.getMessage());

                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e(TAG, "Could not initialize player: " + throwable.getMessage());

                    }
                });
            }
        }
    }




    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }


    @Override
    public void onLoggedIn() {
        Log.d(TAG, "User logged in");
//        mPlayer.playUri(null, "spotify:track:2TpxZ7JUBn3uw46aR7qd6V", 0, 0);
//        requestJsonInfo.getSpotifyInformationApi(this);
    }

    @Override
    public void onLoggedOut() {
        Log.d(TAG, "User logged out");

    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d(TAG, "Login failed");

    }

    @Override
    public void onTemporaryError() {
        Log.d(TAG, "Temporary error occurred");

    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d(TAG, "Received connection message: " + message);

    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d(TAG, "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }

    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d(TAG, "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }

    }



    public void getSpotifyInformationApi(Context context, String name) {


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BASE_URL + name + TYPE_URL)
                .addHeader("Authorization", "Bearer " + HelperClass.getSpotifyToken(context))
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Something wrong with the Request!!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String jsonData = response.body().string();
                artist = new Artist();
                artistArrayList = new ArrayList<>();
                genresList = new ArrayList<>();

                if (response.isSuccessful()) {
                    Log.d(TAG, "SUCCESS: " + jsonData);

                    try {
                        JSONObject jsonDataObject = new JSONObject(jsonData);
                        JSONObject jsonArtistsObject = jsonDataObject.getJSONObject("artists");
                        JSONArray jsonAllItems = jsonArtistsObject.optJSONArray("items");

                        if (jsonAllItems != null) {

                            for (int i = 0; i < jsonAllItems.length(); i++) {
                                JSONObject jsonAllObject = jsonAllItems.getJSONObject(i);

                                if (jsonAllObject != null) {
                                    JSONArray jsonAllImages = jsonAllObject.optJSONArray("images");
                                    JSONArray jsonGenres = jsonAllObject.optJSONArray("genres");

                                    Log.e(TAG, " ");
                                    if(jsonAllImages.length() != 0){
                                        Log.e(TAG, " Image URLS: " + jsonAllImages.getJSONObject(0).getString("url"));
                                        //artist.setImageUrl(jsonAllImages.getJSONObject(0).getString("url"));
                                        Log.e(TAG, " Image Height: " + jsonAllImages.getJSONObject(0).getInt("height"));
                                        //artist.setHeight(jsonAllImages.getJSONObject(0).getInt("height"));
                                    }else{
                                        Log.e(TAG, " Image URLS: []");
                                        Log.e(TAG, " Image Height: []");
                                    }


                                    if (jsonGenres.length() != 0) {
                                        StringBuilder genres = new StringBuilder();

                                        for (int j = 0; j < jsonGenres.length(); j++) {
                                            genresList.add(jsonGenres.getString(j));
                                            genres.append(jsonGenres.getString(j) + ", ");
                                        }
                                        Log.e(TAG, " Genres: " + genres);

                                    } else {
                                        Log.e(TAG, " Genres: []");
                                    }

                                    Log.e(TAG, "<<<Name: " + jsonAllObject.getString("name"));
                                    artist.setName(jsonAllObject.getString("name"));
                                    Log.e(TAG, "<<<Popularity: " + Integer.parseInt(jsonAllObject.getString("popularity")));
                                    artist.setPopularity(Integer.parseInt(jsonAllObject.getString("popularity")));
                                    Log.e(TAG, "<<<Type: " + jsonAllObject.getString("type"));
                                    artist.setType(jsonAllObject.getString("type"));
                                }

                                Log.i(TAG, "artistArrayList size: " + artistArrayList.size());
                                artistArrayList.add(artist);
                            }

                        }

                        Log.i(TAG, "ArrayList size2: " + artistArrayList.size());
                        varHolder.setArtistList(artistArrayList);


                    } catch (Exception e) {
                        Log.e(TAG, "Exception: " + e.getMessage());
                    }
                }

            }
        });

    }





}
