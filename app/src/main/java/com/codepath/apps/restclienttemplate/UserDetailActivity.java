package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class UserDetailActivity extends AppCompatActivity {

    public static final String TAG = "UserDetailActivity";

    TextView tvName;
    TextView tvScreenName;
    TextView tvDescription;
    TextView tvTweetsNum;
    ImageView ivProfileImage;
    ImageView ivBannerImage;
    TextView tvFollowingNum;
    TextView tvFollowersNum;
    TextView tvFollowersTitle;
    TextView tvFollowingTitle;

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
        ivBannerImage = findViewById(R.id.ivBanner);
        tvDescription = findViewById(R.id.tvDescription);
        tvTweetsNum = findViewById(R.id.tvTweetsNum);
        tvFollowersNum = findViewById(R.id.tvFollowersNum);
        tvFollowingNum = findViewById(R.id.tvFollowingNum);
        tvFollowersTitle = findViewById(R.id.tvFollowersTitle);
        tvFollowingTitle = findViewById(R.id.tvFollowingTitle);

        final User user = (User) Parcels.unwrap(getIntent().getParcelableExtra("USER"));

        tvName.setText(user.name);
        tvScreenName.setText("@" + user.screenName);
        tvDescription.setText(user.description);
        tvTweetsNum.setText(Integer.toString(user.numTweets));
        tvFollowingNum.setText(Integer.toString(user.follwingCount));
        tvFollowersNum.setText(Integer.toString(user.followersCount));

        int radius = 30;
        int margin = 10;
        Glide.with(this)
                .load(user.profileImageUrl)
                .transform(new RoundedCornersTransformation(radius, margin))
                .into(ivProfileImage);

        Glide.with(this)
                .load(user.profileBannerUrl)
                .into(ivBannerImage);

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

        tvFollowersTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFollowersClick(user);
            }
        });

        tvFollowersNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFollowersClick(user);
            }
        });

        tvFollowingTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFollowingClick(user);
            }
        });

        tvFollowingNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFollowingClick(user);
            }
        });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.compose) {
            //compose icon has been selected
            //navigate to the compose activity
            showEditDialog();
            return true;
        }

        if (item.getItemId() == R.id.profile) {
            //profile icon has been selected
            Log.i(TAG, "profile clicked");
            Intent i = new Intent (UserDetailActivity.this, ProfileActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance("");
        composeFragment.show(fm, "fragment_edit_name");
    }

    private void showEditDialog(String content) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance(content);
        composeFragment.show(fm, "fragment_edit_name");
    }
}