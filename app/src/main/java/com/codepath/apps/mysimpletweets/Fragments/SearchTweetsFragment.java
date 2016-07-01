package com.codepath.apps.mysimpletweets.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jolenam on 6/30/16.
 */
public class SearchTweetsFragment extends TweetsListFragment {

    private TwitterClient client;
    String search;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        search = getArguments().getString("search");
        Log.d("searchTweetsFragment", search);
        populateSearchResults(search);

    }

    public static SearchTweetsFragment newInstance(String search) {
        SearchTweetsFragment searchFragment = new SearchTweetsFragment();
        Bundle args = new Bundle();
        args.putString("search", search);
        searchFragment.setArguments(args);
        return searchFragment;
    }

    private void populateSearchResults(String search) {
        client.getSearchResults(search, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray searchJSONArray = response.getJSONArray("statuses");
                    ArrayList<Tweet> tweets = Tweet.fromJSONArray(searchJSONArray);

                    clear();

                    // Load model into listview
                    addAll(tweets);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("searchtweetsfragment", errorResponse.toString());
            }
        });
    }

    // NEEDED BECAUSE MADE TWEETSLISTFRAGMENT ABSTRACT
    @Override
    protected void getTweets(String maxId) {

    }




}
