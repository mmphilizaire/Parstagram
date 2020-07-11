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
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{

    private Context mContext;
    private List<Comment> mComments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.mContext = context;
        this.mComments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = mComments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        Log.e("MISHKA", "" + mComments.size());
        return mComments.size();
    }

    public void add(Comment comment){
        mComments.add(comment);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mUsernameTextView;
        private ImageView mProfilePictureImageView;
        private TextView mCommentTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mUsernameTextView = itemView.findViewById(R.id.tvUsername);
            mProfilePictureImageView = itemView.findViewById(R.id.ivProfilePicture);
            mCommentTextView = itemView.findViewById(R.id.tvComment);
        }

        public void bind(Comment comment) {
             mCommentTextView.setText(comment.getComment());
             mUsernameTextView.setText(comment.getUser().getUsername());

             ParseFile profilePicture = comment.getUser().getParseFile("profilePicture");
             Glide.with(mContext).load(profilePicture.getUrl()).into(mProfilePictureImageView);
        }

    }
}
