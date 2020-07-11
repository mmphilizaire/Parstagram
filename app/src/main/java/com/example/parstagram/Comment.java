package com.example.parstagram;

import com.parse.ParseUser;

public class Comment {

    private ParseUser mUser;
    private String mComment;

    public Comment(ParseUser user, String comment) {
        this.mUser = user;
        this.mComment = comment;
    }

    public ParseUser getUser() {
        return mUser;
    }

    public String getComment() {
        return mComment;
    }
}
