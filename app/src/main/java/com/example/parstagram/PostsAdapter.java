package com.example.parstagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>{

    private Context mContext;
    private List<Post> mPosts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.mContext = context;
        this.mPosts = posts;
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

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mUsernameTextView;
        private ImageView mImageView;
        private TextView mDescriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mUsernameTextView = itemView.findViewById(R.id.tvUsername);
            mImageView = itemView.findViewById(R.id.ivImage);
            mDescriptionTextView = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(Post post) {
            mDescriptionTextView.setText(post.getDescription());
            mUsernameTextView.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            if(image != null){
                Glide.with(mContext).load(post.getImage().getUrl()).into(mImageView);
            }
        }
    }



}
