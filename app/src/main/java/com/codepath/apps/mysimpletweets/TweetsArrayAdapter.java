package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

// Takes Tweet objects and turning them into Views (displayed in LV)
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {


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

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("screen_name", tweet.getUser().getScreenName());
                i.putExtra("user", tweet.getUser());
                getContext().startActivity(i);
            }
        });

        // 5. Return the view to be inserted into the list
        return convertView;
    }
}
