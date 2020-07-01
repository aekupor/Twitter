package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
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
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    public static final String TAG = "UsersAdapter";

    Context context;
    List<User> users;

    //pass in the context and list of users
    public UsersAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    //for each row, inflate the layout
    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UsersAdapter.ViewHolder(view);
    }

    //bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int position) {
        //get the data at position
        User user = users.get(position);
        //bind the user with view holder
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    //define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvScreenName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the user at the position
                User user = users.get(position);
                Log.i(TAG, "user clicked: " + user.screenName);

                // create intent for the new activity
                Intent intent = new Intent(context, FollowersActivity.class);
                // serialize the user using parceler
                intent.putExtra("USER_ID", user.idInt);
                // show the activity
                context.startActivity(intent);
            }
        }

        public void bind(User user) {
            tvScreenName.setText(user.screenName);
        }
    }
}
