package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codepath.apps.mysimpletweets.Fragments.SearchTweetsFragment;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        String search = getIntent().getStringExtra("query");
        Log.d("search", search);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("Search results for " + search);

        // Load fragment dynamically
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SearchTweetsFragment searchFragment = SearchTweetsFragment.newInstance(search);
        ft.replace(R.id.flContainer, searchFragment);
        ft.commit();


    }


}
