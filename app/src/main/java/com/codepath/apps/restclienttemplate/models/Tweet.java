package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {

    public static final String TAG = "TweetModel";

    public String body;
    public String createdAt;
    public Integer favorites;
    public Integer retweets;
    public String id;
    public User user;
    public String imageUrl;
    public Integer imageHeight;
    public Integer imageWidth;

    //empty constructor needed for Parceler Library
    public Tweet() {}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.favorites = jsonObject.getInt("favorite_count");
        tweet.retweets = jsonObject.getInt("retweet_count");
        tweet.id = jsonObject.getString("id_str");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        try {
            JSONArray mediaArray = jsonObject.getJSONObject("entities").getJSONArray("media");
            //get first element in mediaArray
            //for all Tweets with more than one photo, will need to use extended_entities
            tweet.imageUrl = mediaArray.getJSONObject(0).getString("media_url_https");
            Log.i(TAG, "imageurl: " + tweet.imageUrl);

            //get size of image
            JSONObject sizes = mediaArray.getJSONObject(0).getJSONObject("sizes");
            tweet.imageHeight = sizes.getJSONObject("medium").getInt("h");
            tweet.imageWidth = sizes.getJSONObject("medium").getInt("w");
            Log.i(TAG, "height: " + tweet.imageHeight.toString() + " width: "+ tweet.imageWidth.toString());
        } catch (JSONException e) {
            Log.i(TAG, "no media");
            tweet.imageUrl = "NULL";
        }
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List <Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
