package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class FollowersActivity extends AppCompatActivity {

    public static final String TAG = "FollowersActivity";

    RecyclerView rvUsers;
    List<User> users;
    UsersAdapter adapter;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        client = TwitterApp.getRestClient(this);

        //find the recycler view
        rvUsers = findViewById(R.id.rvTweets);
        //init the list of users and adapter
        users = new ArrayList<>();
        adapter = new UsersAdapter(this, users);

        Long userId = getIntent().getExtras().getLong("USER_ID");
        Boolean followers = getIntent().getExtras().getBoolean("FOLLOWERS");
        Log.i(TAG, "user id to display: " + userId.toString() + " followers: " + followers.toString());

        if (followers) {
            //make call to getFollowersList
            client.getFollowersList(userId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess call to getFollowersList");
                    
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.i(TAG, "onFailure call to getFollowersList");
                }
            });
        } else {
            //make call to getFollowingList
        }
    }
}