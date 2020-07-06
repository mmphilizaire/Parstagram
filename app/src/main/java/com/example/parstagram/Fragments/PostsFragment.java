package com.example.parstagram.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.parstagram.MainActivity;
import com.example.parstagram.Post;
import com.example.parstagram.PostsAdapter;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class PostsFragment extends Fragment {

    public static final String sTAG = "PostsFragment";

    private RecyclerView mPostsRecyclerView;
    private PostsAdapter mAdapter;
    private List<Post> mPosts;

    public PostsFragment() {
        // Required empty public constructor
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_posts, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPostsRecyclerView = view.findViewById(R.id.rvPosts);

        mPosts = new ArrayList<>();

        mAdapter = new PostsAdapter(getContext(), mPosts);
        mPostsRecyclerView.setAdapter(mAdapter);
        mPostsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        queryPosts();
    }

    private void queryPosts() {

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.sKEY_USER);
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