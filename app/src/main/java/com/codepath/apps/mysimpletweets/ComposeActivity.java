package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.io.Serializable;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ComposeActivity extends AppCompatActivity {

    TwitterClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApplication.getRestClient();

        // Get the account info
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response); // my curent user's account information
                populateProfileHeader(user);
            }
        });

        Button btnTweet = (Button) findViewById(R.id.btnTweet);
        EditText etTweet = (EditText) findViewById(R.id.etTweet);
        final String strTweet = etTweet.getText().toString();

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_LONG).show();
                client.postTweet(strTweet, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Tweet tweet = Tweet.fromJSON(response);

                        Intent data = new Intent();
                        data.putExtra("tweet", (Serializable)tweet);
                        setResult(RESULT_OK, data);
                        finish();
                    }
                });
            }
        });


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
