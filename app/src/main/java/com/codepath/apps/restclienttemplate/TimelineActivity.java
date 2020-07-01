package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity implements ComposeFragment.EditNameDialogListener {

    public static final String TAG = "TimelineActivity";
    public static final int MAX_TWEET_LENGTH = 280;
    public final int REQUEST_CODE = 20;

    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;
    SwipeRefreshLayout swipeContainer;
    EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);

        swipeContainer = findViewById(R.id.swipeContainer);

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching new data");
                populateHomeTimeline();
            }
        });

        //find the recycler view
        rvTweets = findViewById(R.id.rvTweets);
        //init the list of tweets and adapter
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);
        //recycler view setup: layout manager and the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(layoutManager);
        rvTweets.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore: " + page);

                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);

        populateHomeTimeline();
    }

    // Append the next page of data into the adapter
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        client.getNextPageOfTweets(tweets.get(tweets.size() - 1).idInt, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess for loadMore" + json.toString());
                // Deserialize and construct new model objects from the API response
                JSONArray jsonArray = json.jsonArray;
                try {
                    // Append the new data objects to the existing set of items inside the array of items
                    List <Tweet> tweets = Tweet.fromJsonArray(jsonArray);
                    // Notify the adapter of the new items made
                    adapter.addAll(tweets);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "onFailure for loadMore" + throwable);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.compose) {
            //compose icon has been selected
            //navigate to the compose activity
            showEditDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            //get data from the intent (tweet)
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            //update the recycler view with this tweet
            //modify data source of the tweets
            tweets.add(0, tweet);
            //update the adapter
            adapter.notifyItemInserted(0);
            rvTweets.smoothScrollToPosition(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess" + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    adapter.clear();
                    adapter.addAll(Tweet.fromJsonArray(jsonArray));
                    //call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);
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

    // This method is invoked in the activity when the listener is triggered
    // Access the data result passed to the activity here
    public void onFinishEditDialog(String tweetContent) {
        if (tweetContent.isEmpty()) {
            Toast.makeText(TimelineActivity.this, "Sorry, your tweet cannot be empty", Toast.LENGTH_SHORT).show();
            showEditDialog();
        }
        if (tweetContent.length() > MAX_TWEET_LENGTH) {
            Toast.makeText(TimelineActivity.this, "Sorry, your tweet is too long", Toast.LENGTH_SHORT).show();
            showEditDialog(tweetContent);
        }

        //make an API call to Twitter to publish the tweet
        client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    Tweet tweet = Tweet.fromJson(json.jsonObject);
                    Log.i(TAG, "published tweet says: " + tweet.body);
                    Intent intent = new Intent();
                    //set result code and bundle data for response
                    intent.putExtra("tweet", Parcels.wrap(tweet));
                    //close the activity and pass data to parent
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure to publish tweet", throwable);
            }
        });
    }
}