package com.joelnemi.foro;

import android.content.Context;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.joelnemi.foro.utils.Lib;

import java.util.ArrayList;

public class AdaptadorComentarios extends RecyclerView.Adapter<AdaptadorComentarios.ComentarioViewHolder> {
    private ArrayList<Comentario> comentarios;
    private Context context;
    private int position;



    public AdaptadorComentarios(Context context, ArrayList<Comentario> comentarios) {
        this.context = context;
        this.comentarios = comentarios;
    }

    @NonNull
    @Override
    public AdaptadorComentarios.ComentarioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_coments, viewGroup, false);
        AdaptadorComentarios.ComentarioViewHolder viewHolder = new AdaptadorComentarios.ComentarioViewHolder(itemView, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdaptadorComentarios.ComentarioViewHolder holder, final int position) {
        Comentario comentario = comentarios.get(position);
        holder.bindPost(comentario);


    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public static class ComentarioViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView tvNombre;
        private TextView tvTimeElapsed;
        private TextView tvComentario;
        private ImageView ivFotoPerfil;



        public ComentarioViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;


            tvNombre = itemView.findViewById(R.id.tvNameComment);
            tvTimeElapsed = itemView.findViewById(R.id.tvTimeElapsed);
            tvComentario = itemView.findViewById(R.id.tvComment);
            ivFotoPerfil = itemView.findViewById(R.id.ivFotoPerfilComent);


        }

        public void bindPost(final Comentario comentario) {

            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            //DocumentReference docRef = db.collection("users").document(userUID).set(new Usuario());
            db.collection("users").document(comentario.getUserUID()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value.getData() == null) {

                    } else {

                        Usuario usuario = value.toObject(Usuario.class);
                        rellenarDatos(usuario,comentario);
                    }
                }
            });

        }

        public void rellenarDatos(Usuario user, final Comentario comentario) {
            if (user != null) {
                tvNombre.setText(user.getNombre());
                tvComentario.setText(comentario.getComentario());
                String fecha = Lib.getTimeElapsed(comentario.getFecha());
                tvTimeElapsed.setText(fecha);


                if (user.getFoto() != null)
                    if (!user.getFoto().equals(""))
                        Glide.with(context)
                                .load(user.getFoto())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .thumbnail(0.1f)
                                .into(ivFotoPerfil);
            }

        }
    }
}


