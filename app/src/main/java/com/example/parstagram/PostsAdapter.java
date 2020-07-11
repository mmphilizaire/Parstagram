package com.example.parstagram;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>{

    private Context mContext;
    private List<Post> mPosts;
    private boolean mProfileView;

    public PostsAdapter(Context context, List<Post> posts, boolean profileView) {
        this.mContext = context;
        this.mPosts = posts;
        this.mProfileView = profileView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void clear(){
        mPosts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> posts){
        mPosts.addAll(posts);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mUsernameTextView;
        private ImageView mProfilePictureImageView;
        private ImageView mPostImageView;
        private TextView mDescriptionTextView;
        private TextView mTimeTextView;
        private ImageView mLikeImageView;
        private ImageView mCommentImageView;
        private TextView mLikeCountTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            configureViewObjects();
        }

        private void configureViewObjects() {
            mUsernameTextView = itemView.findViewById(R.id.tvUsername);
            mProfilePictureImageView = itemView.findViewById(R.id.ivProfilePicture);
            mPostImageView = itemView.findViewById(R.id.ivImage);
            mDescriptionTextView = itemView.findViewById(R.id.tvDescription);
            mTimeTextView = itemView.findViewById(R.id.tvTime);
            mLikeImageView = itemView.findViewById(R.id.ivLike);
            mCommentImageView = itemView.findViewById(R.id.ivComment);
            mLikeCountTextView = itemView.findViewById(R.id.tvLikeCount);
        }

        public void bind(Post post) {
            if(mProfileView){
                setProfileView();
            }
            else{
                setFeedView(post);
            }

            ParseFile image = post.getImage();
            if(image != null){
                Glide.with(mContext).load(post.getImage().getUrl()).into(mPostImageView);
            }
        }

        private void setFeedView(Post post) {
            mDescriptionTextView.setText(post.getDescription());
            mUsernameTextView.setText(post.getUser().getUsername());

            updateLikes(post);

            ParseFile profilePicture = post.getUser().getParseFile("profilePicture");
            Glide.with(mContext).load(profilePicture.getUrl()).transform(new CircleCrop()).into(mProfilePictureImageView);

            long dateMillis = post.getCreatedAt().getTime();
            String relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            mTimeTextView.setText(relativeDate);

            try {
                if(post.likedBy(ParseUser.getCurrentUser())){
                    mLikeImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ufi_heart_active));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mLikeImageView.setOnClickListener(new LikeClick(post));
        }

        private void setProfileView() {
            mDescriptionTextView.setVisibility(View.GONE);
            mUsernameTextView.setVisibility(View.GONE);
            mLikeImageView.setVisibility(View.GONE);
            mCommentImageView.setVisibility(View.GONE);
            mLikeCountTextView.setVisibility(View.GONE);

            mProfilePictureImageView.setVisibility(View.GONE);

            mTimeTextView.setVisibility(View.GONE);

            mPostImageView.getLayoutParams().height = (int) convertDpToPx(100);
            mPostImageView.requestLayout();
        }

        public class LikeClick implements View.OnClickListener{

            Post mPost;

            public LikeClick(Post post){
                this.mPost = post;
            }

            @Override
            public void onClick(View view) {
                try {
                    if(mPost.likedBy(ParseUser.getCurrentUser())){
                        mPost.removeLike(ParseUser.getCurrentUser());
                        mLikeImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ufi_heart));
                    }
                    else{
                        mPost.addLike(ParseUser.getCurrentUser());
                        mLikeImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ufi_heart_active));
                    }
                    updateLikes(mPost);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mPost.saveInBackground();
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

        private float convertDpToPx(float dp) {
            return dp * ((float) mContext.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION){
                Post post = mPosts.get(position);
                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra("post", Parcels.wrap(post));
                mContext.startActivity(intent);
            }
        }
    }
}
