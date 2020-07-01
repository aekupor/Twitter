package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {

    public String name;
    public String screenName;
    public String profileImageUrl;
    public long idInt;
    public Integer followersCount;
    public Integer friendsCount;

    //empty constructor needed for Parceler Library
    public User(){}

    public static User fromJson (JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.screenName = jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url_https");
        user.idInt = jsonObject.getLong("id");
        user.followersCount = jsonObject.getInt("followers_count");
        user.friendsCount = jsonObject.getInt("friends_count");
        return user;
    }
}
