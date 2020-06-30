package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailActivity extends AppCompatActivity {

    public static final String TAG = "TweetDetailActivity";

    TextView tvScreenName;
    TextView tvBody;
    TextView tvDate;
    ImageView ivProfileImage;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        tvScreenName = findViewById(R.id.tvScreenName);
        tvBody = findViewById(R.id.tvBody);
        tvDate = findViewById(R.id.tvDate);
        ivProfileImage = findViewById(R.id.ivProfileImage);

        // unwrap the movie passed in via intent
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        Log.d(TAG, String.format("Showing details for '%s'", tweet.body));

        tvScreenName.setText(tweet.user.screenName);
        tvBody.setText(tweet.body);
        tvDate.setText(tweet.createdAt);
        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .into(ivProfileImage);
    }
}