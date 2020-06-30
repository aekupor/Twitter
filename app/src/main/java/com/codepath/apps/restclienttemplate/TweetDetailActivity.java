package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class TweetDetailActivity extends AppCompatActivity {

    public static final String TAG = "TweetDetailActivity";

    TextView tvScreenName;
    TextView tvBody;
    TextView tvDate;
    TextView tvFavorites;
    TextView tvRetweets;
    ImageView ivProfileImage;
    Button btnLike;
    Button btnDislike;
    Button btnRetweet;
    Button btnUnretweet;
    Tweet tweet;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        tvScreenName = findViewById(R.id.tvScreenName);
        tvBody = findViewById(R.id.tvBody);
        tvDate = findViewById(R.id.tvDate);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvFavorites = findViewById(R.id.tvFavorites);
        tvRetweets = findViewById(R.id.tvRetweets);
        btnLike = findViewById(R.id.btnLike);
        btnDislike = findViewById(R.id.btnDislike);
        btnRetweet = findViewById(R.id.btnRetweet);
        btnUnretweet = findViewById(R.id.btnUnretweet);

        client = TwitterApp.getRestClient(this);

        // unwrap the movie passed in via intent
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        Log.d(TAG, String.format("Showing details for '%s'", tweet.body));

        tvScreenName.setText(tweet.user.screenName);
        tvBody.setText(tweet.body);
        tvDate.setText(tweet.getRelativeTimeAgo(tweet.createdAt));
        tvFavorites.setText(Integer.toString(tweet.favorites));
        tvRetweets.setText(Integer.toString(tweet.retweets));
        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .into(ivProfileImage);

        //set click listener on button
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TweetDetailActivity.this, "Like tweet", Toast.LENGTH_SHORT).show();
                String tweetId = tweet.id;
                Log.i(TAG, "tweet to like: " + tweetId);
                //make an API call to Twitter to publish the tweet
                client.likeTweet(tweetId, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "liked tweet: " + tweet.id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to like tweet", throwable);
                    }
                });
            }
        });

        btnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TweetDetailActivity.this, "Unlike tweet", Toast.LENGTH_SHORT).show();
                String tweetId = tweet.id;
                Log.i(TAG, "tweet to dislike: " + tweetId);
                //make an API call to Twitter to publish the tweet
                client.dislikeTweet(tweetId, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "disliked tweet: " + tweet.id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to dislike tweet", throwable);
                    }
                });
            }
        });

        btnRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TweetDetailActivity.this, "Retweet", Toast.LENGTH_SHORT).show();
                String tweetId = tweet.id;
                Log.i(TAG, "tweet to retweet: " + tweetId);
                //make an API call to Twitter to publish the tweet
                client.retweet(tweetId, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "retweeted tweet: " + tweet.id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to retweet tweet", throwable);
                    }
                });
            }
        });
    }
}