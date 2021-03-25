package com.joelnemi.foro;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class AdaptadorPosts extends RecyclerView.Adapter<AdaptadorPosts.PostViewHolder> {
    private ArrayList<Post> posts;
    private Context context;
    private int position;


    public AdaptadorPosts(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);
        PostViewHolder viewHolder = new PostViewHolder(itemView, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bindPost(post);


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public static class PostViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView tvFrase;
        private TextView tvAutor;
        private ImageView im1;
        private ImageView im2;
        private Button bUpdate;
        private Button bDelete;

        public PostViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            im1 = itemView.findViewById(R.id.imageView8);
            im2 = itemView.findViewById(R.id.imageView10);

            im1.setImageResource(R.drawable.cake);
            im2.setImageResource(R.drawable.casa);
        }

        public void bindPost(Post post) {

        }

    }

}
