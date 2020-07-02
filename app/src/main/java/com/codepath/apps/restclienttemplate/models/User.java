package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class User {

    public String name;
    public String screenName;
    public String profileImageUrl;
    public String profileBannerUrl;
    public long idInt;
    public Integer followersCount;
    public Integer follwingCount;
    public String description;
    public Integer numTweets;

    //empty constructor needed for Parceler Library
    public User(){}

    public static User fromJson (JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.screenName = jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url_https");
        if (jsonObject.has("profile_banner_url")) {
            user.profileBannerUrl = jsonObject.getString("profile_banner_url");
        }
        user.idInt = jsonObject.getLong("id");
        user.followersCount = jsonObject.getInt("followers_count");
        user.follwingCount = jsonObject.getInt("friends_count");
        user.description = jsonObject.getString("description");
        user.numTweets = jsonObject.getInt("statuses_count");
        return user;
    }

    public static List<User> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Log.i("USER", "getting user");
            users.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return users;
    }
}
