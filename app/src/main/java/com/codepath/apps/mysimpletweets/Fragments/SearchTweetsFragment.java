package com.codepath.apps.mysimpletweets.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;

/**
 * Created by jolenam on 6/30/16.
 */
public class SearchTweetsFragment extends TweetsListFragment {

    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        // user = (User) getIntent().getSerializableExtra("user");

    }

    // NEEDED BECAUSE MADE TWEETSLISTFRAGMENT ABSTRACT
    @Override
    protected void getTweets(String maxId) {

    }




}
