package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
    MenuItem miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        client = TwitterApp.getRestClient(this);

        //find the recycler view
        rvUsers = findViewById(R.id.rvUsers);
        //init the list of users and adapter
        users = new ArrayList<>();
        adapter = new UsersAdapter(this, users);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvUsers.setLayoutManager(layoutManager);
        rvUsers.setAdapter(adapter);

        Long userId = getIntent().getExtras().getLong("USER_ID");
        Boolean followers = getIntent().getExtras().getBoolean("FOLLOWERS");
        Log.i(TAG, "user id to display: " + userId.toString() + " followers: " + followers.toString());

        if (followers) {
            //make call to getFollowersList
            client.getFollowersList(userId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess call to getFollowersList");
                    JSONObject jsonObject = json.jsonObject;
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("users");
                        adapter.addAll(User.fromJsonArray(jsonArray));
                        adapter.notifyDataSetChanged();
                        hideProgressBar();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.i(TAG, "onFailure call to getFollowersList");
                }
            });
        } else {
            //make call to getFollowingList
            client.getFollowingList(userId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess call to getFollowingList");
                    JSONObject jsonObject = json.jsonObject;
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("users");
                        adapter.addAll(User.fromJsonArray(jsonArray));
                        adapter.notifyDataSetChanged();
                        hideProgressBar();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.i(TAG, "onFailure call to getFollowingList");
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu; this add items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Return to finish
        showProgressBar();
        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }
}