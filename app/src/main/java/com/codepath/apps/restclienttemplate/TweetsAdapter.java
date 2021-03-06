package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.apache.commons.cli.ParseException;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    public static final String TAG = "TweetsAdapter";

    Context context;
    List<Tweet> tweets;

    //pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    //for each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    //bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get the data at position
        Tweet tweet = tweets.get(position);
        //bind the tweet with view holder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    //clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    //add a list of items
    public void addAll(List<Tweet> tweetList) {
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }

    //define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvDate;
        ImageView ivMedia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the tweet at the position
                Tweet tweet = tweets.get(position);
                Log.i(TAG, "tweet clicked from user: " + tweet.user.screenName);

                // create intent for the new activity
                Intent intent = new Intent(context, TweetDetailActivity.class);
                // serialize the movie using parceler
                intent.putExtra("tweet", Parcels.wrap(tweet));
                // show the activity
                context.startActivity(intent);
            }
        }

        public void bind(final Tweet tweet) {
            tvBody.setText(tweet.body);
            tvBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // create intent for the new activity
                    Intent intent = new Intent(context, TweetDetailActivity.class);
                    // serialize the movie using parceler
                    intent.putExtra("tweet", Parcels.wrap(tweet));
                    // show the activity
                    context.startActivity(intent);
                }
            });

            tvScreenName.setText(tweet.user.screenName);
            tvDate.setText(tweet.getRelativeTimeAgo(tweet.createdAt));

            int radius = 30;
            int margin = 10;
            Glide.with(context)
                    .load(tweet.user.profileImageUrl)
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivProfileImage);
            if (tweet.imageUrl != null) {
                ivMedia.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(tweet.imageUrl)
                        .apply(new RequestOptions().override(tweet.imageWidth, tweet.imageHeight))
                        .into(ivMedia);
            } else {
                ivMedia.setVisibility(View.GONE);
            }
        }
    }
}
