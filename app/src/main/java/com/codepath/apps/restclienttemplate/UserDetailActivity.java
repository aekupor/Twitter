package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

public class UserDetailActivity extends AppCompatActivity {

    TextView tvName;
    TextView tvScreenName;
    TextView tvDescription;
    TextView tvTweetsNum;
    ImageView ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        tvName = findViewById(R.id.tvName);
        tvScreenName = findViewById(R.id.tvScreenName);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvDescription = findViewById(R.id.tvDescription);
        tvTweetsNum = findViewById(R.id.tvTweetsNum);

        User user = (User) Parcels.unwrap(getIntent().getParcelableExtra("USER"));

        tvName.setText(user.name);
        tvScreenName.setText("@" + user.screenName);
        tvDescription.setText(user.description);
        tvTweetsNum.setText(Integer.toString(user.numTweets));

        Glide.with(this)
                .load(user.profileImageUrl)
                .into(ivProfileImage);
    }
}