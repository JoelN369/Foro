package com.joelnemi.foro;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.joelnemi.foro.utils.Lib;

import java.util.ArrayList;


public class AdaptadorPosts extends RecyclerView.Adapter<AdaptadorPosts.PostViewHolder> {
    private ArrayList<Post> posts;
    private Context context;
    private int position;
    private IOnClickPostListener listener;


    public AdaptadorPosts(Context context, ArrayList<Post> posts, IOnClickPostListener listener) {
        this.context = context;
        this.posts = posts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);
        PostViewHolder viewHolder = new PostViewHolder(itemView, context, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {
        Post post = posts.get(position);
        holder.bindPost(post);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUpdateSelected(posts.get(position));
            }
        });


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
        private IOnClickPostListener listener;
        private TextView tvParrafo;
        private TextView tvnombrePerfil;
        private TextView tvnumComs;
        private TextView tvValoracion;
        private TextView tvCategoria;
        private TextView tvDate;
        private ImageView ivPhotoProfile;
        private ImageView ivFoto;
        private ImageView bMessage;
        private ImageView ivUp;
        private ImageView ivDown;
        private FirebaseAuth mAuth;

        public PostViewHolder(View itemView, Context context, IOnClickPostListener listener) {
            super(itemView);
            this.context = context;
            this.listener = listener;

            tvParrafo = itemView.findViewById(R.id.tvParrafo);
            tvnombrePerfil = itemView.findViewById(R.id.tvNombrePerfil);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            tvnumComs = itemView.findViewById(R.id.tvnumCom);
            tvValoracion = itemView.findViewById(R.id.tvValoracion);
            tvDate = itemView.findViewById(R.id.tvDate);
            bMessage = itemView.findViewById(R.id.ivComments);
            ivUp = itemView.findViewById(R.id.ivUp);
            ivDown = itemView.findViewById(R.id.ivDown);
            mAuth = FirebaseAuth.getInstance();
            ivPhotoProfile = itemView.findViewById(R.id.ivFotoPerfilPost);
            ivFoto = itemView.findViewById(R.id.ivFoto);


        }

        public void bindPost(final Post post) {

            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            //DocumentReference docRef = db.collection("users").document(userUID).set(new Usuario());
            db.collection("users").document(post.getUser()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value.getData() == null) {

                    } else {

                        Usuario usuario = value.toObject(Usuario.class);
                        rellenarDatos(usuario,post);
                    }
                }
            });

        }

        public void rellenarDatos(Usuario user, final Post post) {
            if (user != null) {
                tvParrafo.setText(post.getTexto());
                tvnombrePerfil.setText(user.getNombre());
                tvValoracion.setText(post.getValoracion() + "");
                tvnumComs.setText(post.getComentarios().size() + "");
                tvCategoria.setText(post.getCategoria());

                if (post.getUrlFoto() != null)
                    if (!post.getUrlFoto().equals(""))
                        Glide.with(context)
                                .load(post.getUrlFoto())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .thumbnail(0.1f)
                                .into(ivFoto);
                if (user.getFoto() != null)
                    if (!user.getFoto().equals(""))
                        Glide.with(context)
                                .load(user.getFoto())
                                .apply(RequestOptions.circleCropTransform())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .thumbnail(0.1f)
                                .into(ivPhotoProfile);


                String fecha = Lib.getTimeElapsed(post.getFechaPost());

                tvDate.setText(fecha);

                bMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onUpdateSelected(post);
                    }
                });
                ivDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ivUp.setColorFilter(ContextCompat.getColor(context, R.color.iconColor));
                        ivDown.setColorFilter(ContextCompat.getColor(context, R.color.red));


                    }
                });
                ivUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ivDown.setColorFilter(ContextCompat.getColor(context, R.color.iconColor));
                        ivUp.setColorFilter(ContextCompat.getColor(context, R.color.green));

                    }
                });
            }

        }
    }
}


