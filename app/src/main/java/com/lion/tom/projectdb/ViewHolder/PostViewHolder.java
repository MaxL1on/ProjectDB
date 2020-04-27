package com.lion.tom.projectdb.ViewHolder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lion.tom.projectdb.R;
import com.lion.tom.projectdb.Models.Post;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public ImageView likesView;
    private TextView authorView;
    private TextView bodyView;
    private TextView numLikesView;
    private TextView titleView;

    public PostViewHolder(View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.post_title);
        authorView = itemView.findViewById(R.id.post_author);
        likesView = itemView.findViewById(R.id.likes);
        numLikesView = itemView.findViewById(R.id.post_num_likes);
        bodyView = itemView.findViewById(R.id.post_body);
    }

    public void bindToPost(Post post, View.OnClickListener likeClickListener) {
        titleView.setText(post.title);
        authorView.setText(post.author);
        numLikesView.setText(String.valueOf(post.likesCount));
        bodyView.setText(post.body);
        likesView.setOnClickListener(likeClickListener);
    }
}