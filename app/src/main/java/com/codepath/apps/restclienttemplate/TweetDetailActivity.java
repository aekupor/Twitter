package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class TweetDetailActivity extends AppCompatActivity implements ComposeFragment.EditNameDialogListener {

    public static final String TAG = "TweetDetailActivity";
    public static final int MAX_TWEET_LENGTH = 280;
    public final int TWEET_CODE = 10;

    TextView tvScreenName;
    TextView tvBody;
    TextView tvDate;
    TextView tvFavorites;
    TextView tvRetweets;
    ImageView ivProfileImage;
    ImageView ivMedia;
    Button btnLike;
    Button btnDislike;
    Button btnRetweet;
    Button btnUnretweet;
    Button btnReply;
    Tweet tweet;
    TwitterClient client;

    String userToReply;
    Long replyTweetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        tvScreenName = findViewById(R.id.tvScreenName);
        tvBody = findViewById(R.id.tvBody);
        tvDate = findViewById(R.id.tvDate);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        ivMedia = findViewById(R.id.ivMedia);
        tvFavorites = findViewById(R.id.tvFavorites);
        tvRetweets = findViewById(R.id.tvRetweets);
        btnLike = findViewById(R.id.btnLike);
        btnDislike = findViewById(R.id.btnDislike);
        btnRetweet = findViewById(R.id.btnRetweet);
        btnUnretweet = findViewById(R.id.btnUnretweet);
        btnReply = findViewById(R.id.btnReply);

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
        if (tweet.imageUrl != null) {
            Log.i(TAG, "image: " + tweet.imageUrl);
            ivMedia.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(tweet.imageUrl)
                    .apply(new RequestOptions().override(tweet.imageWidth, tweet.imageHeight))
                    .into(ivMedia);
        }

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

        btnUnretweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TweetDetailActivity.this, "Unretweet", Toast.LENGTH_SHORT).show();
                String tweetId = tweet.id;
                Log.i(TAG, "tweet to unretweet: " + tweetId);
                //make an API call to Twitter to publish the tweet
                client.unretweet(tweetId, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "unretweeted tweet: " + tweet.id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to unretweet tweet", throwable);
                    }
                });
            }
        });

        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userToReply = tweet.user.screenName;
                replyTweetId = tweet.idInt;
                Log.i(TAG, "reply to user: " + userToReply);
                showEditDialog();
            }
        });
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance(userToReply);
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
            Toast.makeText(TweetDetailActivity.this, "Sorry, your tweet cannot be empty", Toast.LENGTH_SHORT).show();
            showEditDialog();
        }
        if (tweetContent.length() > MAX_TWEET_LENGTH) {
            Toast.makeText(TweetDetailActivity.this, "Sorry, your tweet is too long", Toast.LENGTH_SHORT).show();
            showEditDialog(tweetContent);
        }

        //make an API call to Twitter to reply to tweet
        client.replyTweet(tweetContent, replyTweetId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    Tweet tweet = Tweet.fromJson(json.jsonObject);
                    Log.i(TAG, "replied published tweet says: " + tweet.body);
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