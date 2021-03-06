package com.codepath.apps.mysimpletweets.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by jolenam on 6/27/16.
 */

// Parse the JSON + store data, encapsulate state logic or display logic
public class Tweet implements Serializable{

    // list out attributes
    private String body;
    private long uid;
    private User user;
    private String createdAt;
    private String inReplyToUserId;
    private int retweetCount;
    private int favoriteCount;

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public boolean isRetweeted() {
        return isRetweeted;
    }

    private boolean isFavorited;

    public void setRetweeted(boolean retweeted) {
        isRetweeted = retweeted;
    }

    public void setFavorited(boolean favorited) {
        isFavorited = favorited;
    }

    private  boolean isRetweeted;

    public String getCreatedAt() {
        return createdAt;
    }

    public String getBody() {
        return body;
    }

    public String getUid() {
        return String.valueOf(uid);
    }

    public Long getLongUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public String getInReplyToUserId() {
        return inReplyToUserId;
    }

    public int getRetweetCount() { return retweetCount;
    }

    // Deserialize JSON and build Tweet objects
    // Tweet.fromJSON("{...{{ => <Tweet>
    public static Tweet fromJSON(JSONObject jsonObject) {

        Tweet tweet = new Tweet();

        // Extract values from json, store them
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.inReplyToUserId = jsonObject.isNull("in_reply_to_screen_name") ? "" : jsonObject
                    .getString("in_reply_to_screen_name");
            tweet.retweetCount = jsonObject.optInt("retweet_count");
            tweet.isFavorited = jsonObject.getBoolean("favorited");
            tweet.isRetweeted = jsonObject.getBoolean("retweeted");
            tweet.favoriteCount = jsonObject.optInt("favorite_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    // Tweet.fromJSONArray({...}, {..}) => List<Tweet>
    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();

        // Iterate JSON Array and create tweets
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
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
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
