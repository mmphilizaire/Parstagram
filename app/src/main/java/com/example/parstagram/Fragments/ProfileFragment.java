package com.example.parstagram.Fragments;

import android.util.Log;

import com.example.parstagram.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment {

    @Override
    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.sKEY_USER);
        query.whereEqualTo(Post.sKEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.sKEY_CREATEDAT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null){
                    Log.e(sTAG, "Issue with getting posts", e);
                    return;
                }
                for(Post post : posts){
                    Log.i(sTAG, "Post: " + post.getDescription() + ", usernmae: " + post.getUser().getUsername());
                }
                mPosts.addAll(posts);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
