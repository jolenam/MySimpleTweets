package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ComposeActivity extends AppCompatActivity {

    TwitterClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_exit);

        client = TwitterApplication.getRestClient();

        // Get the account info
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response); // my curent user's account information
                populateProfileHeader(user);
            }
        });

        final TextView tvCount = (TextView) findViewById(R.id.tvCount);
        final EditText etTweet = (EditText) findViewById(R.id.etTweet);

        // Changing character count
        etTweet.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // this will show characters remaining
                int length = String.valueOf(s).length();
                int count = 140 - length;
                tvCount.setText(String.valueOf(count));
            }
        });


        Button btnTweet = (Button) findViewById(R.id.btnTweet);


        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTweet = etTweet.getText().toString();
                client.postTweet(strTweet, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("debug", response.toString());
                        Tweet tweet = Tweet.fromJSON(response);

                        Intent data = new Intent();
                        data.putExtra("tweet", tweet);
                        setResult(RESULT_OK, data);
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("debug", errorResponse.toString());
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void populateProfileHeader(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvUsername = (TextView) findViewById(R.id.tvUsername);

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName.setText(user.getName());
        tvUsername.setText("@ " + user.getScreenName());

        Picasso.with(this)
                .load(user.getProfileImageUrl())
                .transform(new RoundedCornersTransformation(3, 3))
                .into(ivProfileImage);
    }
}
