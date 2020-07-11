package com.example.parstagram.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parstagram.EndlessRecyclerViewScrollListener;
import com.example.parstagram.LoginActivity;
import com.example.parstagram.Post;
import com.example.parstagram.PostsAdapter;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment {

    private Button mLogoutButton;
    private ImageView mProfilePictureImageView;
    private TextView mUsernameTextView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setVisible(view);

        Glide.with(getContext()).load(ParseUser.getCurrentUser().getParseFile("profilePicture").getUrl()).transform(new CircleCrop()).into(mProfilePictureImageView);
        mUsernameTextView.setText(ParseUser.getCurrentUser().getUsername());

        mAdapter = new PostsAdapter(getContext(), mPosts, true);
        mPostsRecyclerView.setAdapter(mAdapter);

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent logout = new Intent(getContext(), LoginActivity.class);
                startActivity(logout);
                getActivity().finish();
            }
        });

        setLayout();

    }

    private void setLayout() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mPostsRecyclerView.setLayoutManager(gridLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextData(page);
            }
        };
        mPostsRecyclerView.addOnScrollListener(scrollListener);
    }

    private void setVisible(View view) {
        mProfilePictureImageView = view.findViewById(R.id.ivProfilePicture);
        mUsernameTextView = view.findViewById(R.id.tvUsername);
        mLogoutButton = view.findViewById(R.id.btnLogout);

        mProfilePictureImageView.setVisibility(View.VISIBLE);
        mUsernameTextView.setVisibility(View.VISIBLE);
        mLogoutButton.setVisibility(View.VISIBLE);
    }



    @Override
    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATEDAT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                mPosts.addAll(posts);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
