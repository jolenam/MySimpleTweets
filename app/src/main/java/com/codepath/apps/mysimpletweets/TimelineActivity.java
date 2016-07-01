package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.Fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.Fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;

public class TimelineActivity extends AppCompatActivity {

    SmartFragmentStatePagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Get viewpager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        // Set viewpager adapter for pager
        pagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(pagerAdapter);
        // Find pager sliding tabs
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach pager tabs to viewpager
        tabStrip.setViewPager(vpPager);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Tweet tweet = (Tweet) data.getSerializableExtra("tweet");
            HomeTimelineFragment fragmentHomeTweets =
                    (HomeTimelineFragment) pagerAdapter.getRegisteredFragment(0);
            fragmentHomeTweets.appendTweet(tweet);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                i.putExtra("query", query);
                startActivity(i);

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onProfileView(MenuItem mi) {
        // Launch profile view
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    private final int REQUEST_CODE = 20;

    public void onCompose(MenuItem item) {
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    // Return order of fragments in view pager
    public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter{
        private String tabTitles[] = {"Home", "Mentions"};

        // Adapter gets the manager to insert or remove fragment from activity
        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // order and creation of fragments within pager
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        // Return tab title
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        // Tells how many fragments to swipe between
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }




}
