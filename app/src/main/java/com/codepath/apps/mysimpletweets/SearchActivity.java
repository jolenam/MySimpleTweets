package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.mysimpletweets.Fragments.SearchTweetsFragment;
import com.codepath.apps.mysimpletweets.models.User;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (savedInstanceState == null) {
            SearchTweetsFragment searchFragment = (SearchTweetsFragment)
                    getSupportFragmentManager().findFragmentById(R.id.search_fragment);
        }


        populateResults();
    }

    public void populateResults() {

    }
}
