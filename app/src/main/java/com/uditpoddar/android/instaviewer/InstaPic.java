package com.uditpoddar.android.instaviewer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by upoddar on 11/10/15.
 */
@Data
public class InstaPic {
    private List<String> tags;
    private List<InstaPicComment> comments;
    private String createdTime;
    private long likes;
    private String imageLink;
    private String videoLink;
    private String caption;
    private String type;
    private String userName;
    private String userProfilePicLink;

    /*
     * Creates a new Insta Pic object from the response object of https://api.instagram.com/v1/media/popular?client_id=7a47e3faf99c4bc092b3b0779a05d854 http call
     */
    public static InstaPic of(JSONObject jsonObject) throws JSONException {
        InstaPic instaPic = new InstaPic();

        List<String> tags = new ArrayList<String>();
        JSONArray jsonTags = jsonObject.optJSONArray("tags");
        if ( jsonTags != null) {
            for (int i = 0; i < jsonTags.length(); i++) {
                tags.add(jsonTags.getString(i));
            }
        }
        instaPic.setTags(tags);

        List<InstaPicComment> comments = new ArrayList<InstaPicComment>();
        if ( jsonObject.optJSONObject("comments") != null && jsonObject.getJSONObject("comments").optJSONArray("data") != null) {
            JSONArray jsonComments = jsonObject.getJSONObject("comments").getJSONArray("data");
            for (int i = 0; i < jsonComments.length(); i++) {
                JSONObject jsonComment = jsonComments.getJSONObject(i);
                comments.add(new InstaPicComment(jsonComment.getJSONObject("from").getString("username"), jsonComment.getString("text")));
            }
        }
        instaPic.setComments(comments);

        long createdSince = System.currentTimeMillis() / 1000 - jsonObject.optLong("created_time"); // in seconds
        String suffix = "";
        if ( createdSince < 60 ) {
            suffix = "s";
        } else {
            createdSince /= 60; // in minutes
            if ( createdSince  < 60 ) {
                suffix = "m";
            } else {
                createdSince /= 60; // in hours
                if ( createdSince < 24 ) {
                    suffix = "h";
                } else {
                    createdSince /= 24; // in days
                    if ( createdSince < 30 ) {
                        suffix = "d";
                    } else {
                        createdSince /= 30; // in months
                        if ( createdSince < 12 ) {
                            suffix = "M";
                        } else {
                            createdSince /= 12;
                            suffix = "y";
                        }
                    }
                }
            }
        }
        instaPic.setCreatedTime(String.format("%d%s", createdSince, suffix));

        if ( jsonObject.optJSONObject("likes") != null )
            instaPic.setLikes(jsonObject.getJSONObject("likes").optLong("count"));

        if ( jsonObject.optJSONObject("images") != null && jsonObject.getJSONObject("images").optJSONObject("standard_resolution") != null)
            instaPic.setImageLink(jsonObject.getJSONObject("images").getJSONObject("standard_resolution").optString("url"));

        if ( jsonObject.optJSONObject("videos") != null && jsonObject.getJSONObject("videos").optJSONObject("standard_resolution") != null)
            instaPic.setVideoLink(jsonObject.getJSONObject("videos").getJSONObject("low_resolution").optString("url"));

        if ( jsonObject.optJSONObject("caption") != null)
            instaPic.setCaption(jsonObject.getJSONObject("caption").optString("text"));

        instaPic.setType(jsonObject.optString("type"));
        JSONObject user = jsonObject.optJSONObject("user");
        if ( user != null) {
            instaPic.setUserName(user.optString("full_name"));
            instaPic.setUserProfilePicLink(user.optString("profile_picture"));
        }

        return instaPic;
    }
}
