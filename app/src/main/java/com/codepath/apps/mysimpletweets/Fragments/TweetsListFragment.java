package com.codepath.apps.mysimpletweets.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.ArrayList;
import java.util.List;

public abstract class TweetsListFragment extends Fragment {

    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;

    private SwipeRefreshLayout swipeContainer;

    // inflation logic

    // things that reference the view!
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        // Find listview
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        //Connect adapter to list view
        lvTweets.setAdapter(aTweets);
        // Get client
        return v;

      /* lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // for first request, only specify count, not max_id (https://dev.twitter.com/rest/public/timelines)
                if (aTweets.getCount() == 0) {
                    populateTimeline(null);
                }
                else {
                    Tweet oldestTweet = aTweets.getItem(aTweets.getCount() - 1);
                    String oldId = oldestTweet.getUid();
                    populateTimeline(oldId);
                }
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });*/


        /*// Swipe-to-Refresh
        swipeContainer = (SwipeRefreshLayout) getView().findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimeline(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);*/
    }


    // creation lifecycle event
    // if doesn't reference view
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create arraylist
        tweets = new ArrayList<Tweet>();
        // Construct adapter from data source
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
    }

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    public void appendTweet(Tweet tweet) {
        tweets.add(0, tweet);
        aTweets.notifyDataSetChanged();
        lvTweets.setSelection(0);
    }

    //protected abstract void populateTimeline(String maxId);



}
