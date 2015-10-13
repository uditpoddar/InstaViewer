package com.uditpoddar.android.instaviewer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by upoddar on 12/10/15.
 */
public class InstaPicAdapter extends ArrayAdapter<InstaPic> {

    public InstaPicAdapter(Context context, List<InstaPic> instaPics) {
        super(context, 0, instaPics);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        InstaPic instaPic = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_picture, parent, false);
        }
//        ImageView ivUserThumb = (ImageView) convertView.findViewById(R.id.ivUserThumb);
        RoundedImageView ivUserThumb = (RoundedImageView)convertView.findViewById(R.id.ivUserThumb);
        final ImageView ivPicture = (ImageView) convertView.findViewById(R.id.ivPicture);
        final VideoView vvPicture = (VideoView) convertView.findViewById(R.id.vvPicture);

        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvRelativeTS = (TextView) convertView.findViewById(R.id.tvRelativeTS);
        TextView tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikeCount);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);

        Picasso.with(getContext()).load(instaPic.getUserProfilePicLink()).into(ivUserThumb);

        Picasso.with(getContext()).load(instaPic.getImageLink()).into(ivPicture);
        if ( instaPic.getVideoLink() != null ) {
//            Toast.makeText(getContext(), "Downloading Video from : " + instaPic.getVideoLink(), Toast.LENGTH_SHORT).show();
            vvPicture.setVideoURI(Uri.parse(instaPic.getVideoLink()));
//            vvPicture.setVideoPath(instaPic.getVideoLink());
            MediaController mediaController = new MediaController(getContext());
            mediaController.setAnchorView(vvPicture);
            vvPicture.setMediaController(mediaController);
            vvPicture.requestFocus();
            vvPicture.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
//                    Toast.makeText(getContext(), "Starting Video", Toast.LENGTH_SHORT).show();
                    vvPicture.bringToFront();
                    vvPicture.start();
                    ivPicture.setVisibility(View.INVISIBLE);
                }
            });
        }
        tvUsername.setText(instaPic.getUserName());
        tvRelativeTS.setText(String.valueOf(instaPic.getCreatedTime()));
        tvLikesCount.setText(String.format("%s likes", NumberFormat.getNumberInstance().format(instaPic.getLikes())));
        tvCaption.setText(instaPic.getCaption());

        // Return the completed view to render on screen
        return convertView;
    }
}
