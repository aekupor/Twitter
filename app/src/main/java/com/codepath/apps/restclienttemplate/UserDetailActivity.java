package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class UserDetailActivity extends AppCompatActivity {

    public static final String TAG = "UserDetailActivity";

    TextView tvName;
    TextView tvScreenName;
    TextView tvDescription;
    TextView tvTweetsNum;
    ImageView ivProfileImage;
    TextView tvFollowingNum;
    TextView tvFollowersNum;

    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;
    MenuItem miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        tvName = findViewById(R.id.tvName);
        tvScreenName = findViewById(R.id.tvScreenName);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvDescription = findViewById(R.id.tvDescription);
        tvTweetsNum = findViewById(R.id.tvTweetsNum);
        tvFollowersNum = findViewById(R.id.tvFollowersNum);
        tvFollowingNum = findViewById(R.id.tvFollowingNum);

        User user = (User) Parcels.unwrap(getIntent().getParcelableExtra("USER"));

        tvName.setText(user.name);
        tvScreenName.setText("@" + user.screenName);
        tvDescription.setText(user.description);
        tvTweetsNum.setText(Integer.toString(user.numTweets));
        tvFollowingNum.setText(Integer.toString(user.follwingCount));
        tvFollowersNum.setText(Integer.toString(user.followersCount));

        Glide.with(this)
                .load(user.profileImageUrl)
                .into(ivProfileImage);

        client = TwitterApp.getRestClient(this);

        //find the recycler view
        rvTweets = findViewById(R.id.rvTweets);
        //init the list of tweets and adapter
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);
        //recycler view setup: layout manager and the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(layoutManager);
        rvTweets.setAdapter(adapter);

        populateUserTimeline(user);
    }

    private void populateUserTimeline(User user) {
        client.getUserTweets(user.idInt, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess" + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    adapter.clear();
                    adapter.addAll(Tweet.fromJsonArray(jsonArray));
                    hideProgressBar();
                } catch (JSONException e) {
                    Log.e(TAG, "json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "onFailure", throwable);
            }
        });
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

    public void onFollowersClick(User user) {
        Log.i(TAG, "followers button clicked");
        Intent i = new Intent(UserDetailActivity.this, FollowersActivity.class);
        i.putExtra("USER_ID", user.idInt);
        i.putExtra("FOLLOWERS", true);
        startActivity(i);
    }

    public void onFollowingClick(User user) {
        Log.i(TAG, "following button clicked");
        Intent i = new Intent(UserDetailActivity.this, FollowersActivity.class);
        i.putExtra("USER_ID", user.idInt);
        i.putExtra("FOLLOWERS", false);
        startActivity(i);
    }
}