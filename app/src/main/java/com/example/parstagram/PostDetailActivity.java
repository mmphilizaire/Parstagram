package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    public static final String TAG = "PostDetailActivity";

    private RecyclerView mCommentsRecyclerView;
    protected CommentsAdapter mAdapter;

    private TextView mUsernameTextView;
    private ImageView mProfilePictureImageView;
    private ImageView mPostImageView;
    private TextView mDescriptionTextView;
    private TextView mTimeTextView;
    private ImageView mLikeImageView;
    private ImageView mCommentImageView;
    private TextView mLikeCountTextView;
    private EditText mAddCommentEditText;

    private Post mPost;
    private List<Comment> mComments;

    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        setUpToolBar();

        configureViewObjects();

        mPost = Parcels.unwrap(getIntent().getParcelableExtra("post"));
        mComments = new ArrayList<>();

        mAdapter = new CommentsAdapter(this, mComments);
        mCommentsRecyclerView.setAdapter(mAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mCommentsRecyclerView.setLayoutManager(linearLayoutManager);

        inputValues();

        try {
            addComments();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mLikeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(mPost.likedBy(ParseUser.getCurrentUser())){
                        mPost.removeLike(ParseUser.getCurrentUser());
                        mLikeImageView.setImageDrawable(getResources().getDrawable(R.drawable.ufi_heart));
                    }
                    else{
                        mPost.addLike(ParseUser.getCurrentUser());
                        mLikeImageView.setImageDrawable(getResources().getDrawable(R.drawable.ufi_heart_active));
                    }
                    updateLikes(mPost);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mPost.saveInBackground();
            }
        });

    }

    private void setUpToolBar() {
        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void configureViewObjects() {
        mCommentsRecyclerView = (RecyclerView) findViewById(R.id.rvComments);
        mUsernameTextView = (TextView) findViewById(R.id.tvUsername);
        mProfilePictureImageView = (ImageView) findViewById(R.id.ivProfilePicture);
        mPostImageView = (ImageView) findViewById(R.id.ivImage);
        mDescriptionTextView = (TextView) findViewById(R.id.tvDescription);
        mTimeTextView = (TextView) findViewById(R.id.tvTime);
        mLikeImageView = (ImageView) findViewById(R.id.ivLike);
        mCommentImageView = (ImageView) findViewById(R.id.ivComment);
        mLikeCountTextView = (TextView) findViewById(R.id.tvLikeCount);
        mAddCommentEditText = (EditText) findViewById(R.id.etAddComment);
    }

    private void inputValues(){

        mDescriptionTextView.setText(mPost.getDescription());
        mUsernameTextView.setText(mPost.getUser().getUsername());

        updateLikes(mPost);

        ParseFile profilePicture = mPost.getUser().getParseFile("profilePicture");
        Glide.with(this).load(profilePicture.getUrl()).transform(new CircleCrop()).into(mProfilePictureImageView);

        long dateMillis = mPost.getCreatedAt().getTime();
        String relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        mTimeTextView.setText(relativeDate);

        try {
            if(mPost.likedBy(ParseUser.getCurrentUser())){
                mLikeImageView.setImageDrawable(getResources().getDrawable(R.drawable.ufi_heart_active));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParseFile image = mPost.getImage();
        if(image != null){
            Glide.with(this).load(mPost.getImage().getUrl()).into(mPostImageView);
        }

    }

    private void addComments() throws JSONException {
        JSONArray comments = mPost.getComments();
        for(int i = 0; i < comments.length(); i++){
            final JSONObject comment = comments.getJSONObject(i);
            ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
            query.getInBackground(comment.getString("user"), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser object, ParseException e) {
                    if(e != null){
                        Log.e(TAG, "Issue with getting comments", e);
                        return;
                    }
                    try {
                        mAdapter.add(new Comment(object, comment.getString("comment")));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    private void updateLikes(Post post){
        Number likeCount = post.getLikeCount();
        if(likeCount == (Number)1){
            mLikeCountTextView.setText(likeCount + " like");
        }
        else{
            mLikeCountTextView.setText(likeCount + " likes");
        }
    }

    public void postComment(View view) throws JSONException {
        if(String.valueOf(mAddCommentEditText.getText()).equals("")){
            Toast.makeText(this, "Must include a comment", Toast.LENGTH_LONG).show();
        }
        else{
            mPost.addComment(ParseUser.getCurrentUser(), String.valueOf(mAddCommentEditText.getText()));
            mPost.saveInBackground();
        }
        mAdapter.add(new Comment(ParseUser.getCurrentUser(), String.valueOf(mAddCommentEditText.getText())));
        mAddCommentEditText.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        return true;
    }

}