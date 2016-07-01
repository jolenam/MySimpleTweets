package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

// Takes Tweet objects and turning them into Views (displayed in LV)
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private TwitterClient client;

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    // Override and setup custom template

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Get tweet
        final Tweet tweet = getItem(position);

        // 2. Find or inflate template
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }

        // 3. Find the subviews to fill with data in template
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvFullName = (TextView) convertView.findViewById(R.id.tvFullName);
        TextView tvRelTime = (TextView) convertView.findViewById(R.id.tvRelTime);
        Button btnReply = (Button) convertView.findViewById(R.id.btnReply);
        Button btnRT = (Button) convertView.findViewById(R.id.btnRT);
        Button btnLike = (Button) convertView.findViewById(R.id.btnLike);

        final TextView RTCount = (TextView) convertView.findViewById(R.id.RTCount);
        final TextView favCount = (TextView) convertView.findViewById(R.id.favCount);

        // 4. Populate data into subviews
        tvUsername.setText("@" + tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvRelTime.setText(TimeFormatter.getTimeDifference(tweet.getCreatedAt()));
        tvFullName.setText(tweet.getUser().getName());
        ivProfileImage.setImageResource(android.R.color.transparent); // clear out old image for recycled view
        Picasso.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .transform(new RoundedCornersTransformation(3, 3))
                .into(ivProfileImage);

        if (tweet.isRetweeted()) {
            btnRT.getBackground().setColorFilter(Color.parseColor("#19CF86"), PorterDuff.Mode.SRC_ATOP);
            //RTCount.setTextColor(Color.parseColor("#19CF86"));
        }
        else {
            btnRT.getBackground().setColorFilter(Color.parseColor("#AAB8C2"), PorterDuff.Mode.SRC_ATOP);
        }

        if (tweet.isFavorited()) {
            btnLike.getBackground().setColorFilter(Color.parseColor("#E81C4F"), PorterDuff.Mode.SRC_ATOP);
            //favCount.setTextColor(Color.parseColor("#E81C4F"));
        } else {
            btnLike.getBackground().setColorFilter(Color.parseColor("#AAB8C2"), PorterDuff.Mode.SRC_ATOP);
        }

        if (tweet.getRetweetCount() > 0) {
            RTCount.setText(String.valueOf(tweet.getRetweetCount()));
        }

        if (tweet.getFavoriteCount() > 0) {
            favCount.setText(String.valueOf(tweet.getFavoriteCount()));
        }

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("screen_name", tweet.getUser().getScreenName());
                i.putExtra("user", tweet.getUser());
                getContext().startActivity(i);
            }
        });

        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReplyActivity.class);
                intent.putExtra("userID", tweet.getUser().getUid());
                intent.putExtra("tweet", tweet);
                getContext().startActivity(intent);

            }
        });

        btnRT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                client = TwitterApplication.getRestClient();

               if (tweet.isRetweeted()) {
                    client.unRetweet(tweet.getLongUid(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("DEBUG", errorResponse.toString());
                        }
                    });

                   v.getBackground().setColorFilter(Color.parseColor("#AAB8C2"), PorterDuff.Mode.SRC_ATOP);
                   RTCount.setTextColor(Color.parseColor("#AAB8C2"));
                   tweet.setRetweeted(false);

                   int displayedCount = Integer.parseInt((String)RTCount.getText()) - 1;
                   if (displayedCount > 0) {
                       RTCount.setText(String.valueOf(displayedCount));
                   }

                }

               else {
                   client.postRetweet(tweet.getLongUid(), new JsonHttpResponseHandler() {
                       @Override
                       public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                           Log.d("retweet", response.toString());
                       }



                       @Override
                       public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                       }
                   });

                   int displayedCount = Integer.parseInt((String)RTCount.getText()) + 1;
                   if (displayedCount > 0) {
                       RTCount.setText(String.valueOf(displayedCount));
                   }

                   v.getBackground().setColorFilter(Color.parseColor("#19CF86"), PorterDuff.Mode.SRC_ATOP);
                   RTCount.setTextColor(Color.parseColor("#19CF86"));
                   tweet.setRetweeted(true);
               }

            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                client = TwitterApplication.getRestClient();

                if (tweet.isFavorited()) {
                    client.unLike(tweet.getLongUid(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                        }
                    });
                    tweet.setFavorited(false);
                    v.getBackground().setColorFilter(Color.parseColor("#AAB8C2"), PorterDuff.Mode.SRC_ATOP);
                    favCount.setTextColor(Color.parseColor("#AAB8C2"));
                    int displayedCount = Integer.parseInt((String)favCount.getText()) - 1;
                    if (displayedCount > 0) {
                        favCount.setText(String.valueOf(displayedCount));
                    }
                }
                else {
                    client.postLike(tweet.getLongUid(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("LIKETWEET", response.toString());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("LIKETWEET", errorResponse.toString());
                        }
                    });

                    tweet.setFavorited(true);
                    v.getBackground().setColorFilter(Color.parseColor("#E81C4F"), PorterDuff.Mode.SRC_ATOP);
                    favCount.setTextColor(Color.parseColor("#E81C4F"));
                    int displayedCount = Integer.parseInt((String)favCount.getText()) + 1;
                    if (displayedCount > 0) {
                        favCount.setText(String.valueOf(displayedCount));
                    }
                }
            }
        });

        // 5. Return the view to be inserted into the list
        return convertView;
    }
}
