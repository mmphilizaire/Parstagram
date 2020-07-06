package com.example.parstagram;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String sKEY_DESCRIPTION = "description";
    public static final String sKEY_IMAGE = "image";
    public static final String sKEY_USER = "user";

    public String getDescription(){
        return getString(sKEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(sKEY_DESCRIPTION, description);
    }

    public ParseFile getImage(){
        return getParseFile(sKEY_IMAGE);
    }

    public void setImage(ParseFile parseFile){
        put(sKEY_IMAGE, parseFile);
    }

    public ParseUser getUser(){
        return getParseUser(sKEY_USER);
    }

    public void setUser(ParseUser user){
        put(sKEY_USER, user);
    }

}
