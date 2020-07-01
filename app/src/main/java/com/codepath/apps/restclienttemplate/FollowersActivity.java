package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class FollowersActivity extends AppCompatActivity {

    public static final String TAG = "FollowersActivity";

    RecyclerView rvUsers;
    List<User> users;
    UsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        //find the recycler view
        rvUsers = findViewById(R.id.rvTweets);
        //init the list of users and adapter
        users = new ArrayList<>();
        adapter = new UsersAdapter(this, users);

        Long userId = getIntent().getExtras().getLong("USER_ID");
        Log.i(TAG, "user id to display: " + userId);
    }
}