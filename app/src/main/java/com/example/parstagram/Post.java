package com.example.parstagram;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.sql.Array;
import java.util.Date;

@Parcel(analyze = Post.class)
@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATEDAT = "createdAt";
    public static final String KEY_LIKE_COUNT = "likeCount";
    public static final String KEY_LIKES = "likes";
    public static final String KEY_COMMENTS = "comments";

    public Post(){
    }

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public Number getLikeCount(){
        return getNumber(KEY_LIKE_COUNT);
    }

    public void setLikeCount(Number likeCount){
        put(KEY_LIKE_COUNT, likeCount);
    }

    public JSONArray getLikes(){
        return getJSONArray(KEY_LIKES);
    }

    public void addLike(ParseUser user) throws JSONException {
        Log.e("Mishka", "" + getLikeCount());
        put(KEY_LIKE_COUNT, (Number)((int)getLikeCount()+1));
        JSONObject userLike = new JSONObject();
        userLike.put(user.getUsername(), user);
        Log.e("Mishka", getLikes().toString());
        JSONArray likes = getLikes().put(userLike);
        put(KEY_LIKES, likes);
    }

    public void removeLike(ParseUser user) throws JSONException {
        put(KEY_LIKE_COUNT, (Number)((int)getLikeCount()-1));
        JSONArray likes = getLikes();
        for(int i = 0; i < likes.length(); i++){
            if(likes.getJSONObject(i).has(user.getUsername())){
                likes.remove(i);
            }
        }
        put(KEY_LIKES,likes);
    }

    public boolean likedBy(ParseUser user) throws JSONException {
        JSONArray likes = getLikes();
        for(int i = 0; i < likes.length(); i++){
            if(likes.getJSONObject(i).has(user.getUsername())){
                return true;
            }
        }
        return false;
    }

    public JSONArray getComments(){
        return getJSONArray(KEY_COMMENTS);
    }

    public void addComment(ParseUser user, String comment) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", user.getObjectId());
        jsonObject.put("comment", comment);
        put(KEY_COMMENTS, getComments().put(jsonObject));
    }

}
