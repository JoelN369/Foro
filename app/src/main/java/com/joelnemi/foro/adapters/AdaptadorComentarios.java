package com.joelnemi.foro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.joelnemi.foro.listeners.IComentClickListener;
import com.joelnemi.foro.models.Comentario;
import com.joelnemi.foro.R;
import com.joelnemi.foro.models.Post;
import com.joelnemi.foro.models.Usuario;
import com.joelnemi.foro.utils.Lib;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdaptadorComentarios extends RecyclerView.Adapter<AdaptadorComentarios.ComentarioViewHolder> {
    private ArrayList<Comentario> comentarios;
    private Context context;
    private int position;
    private IComentClickListener listener;


    public AdaptadorComentarios(Context context, ArrayList<Comentario> comentarios, IComentClickListener listener) {
        this.context = context;
        this.comentarios = comentarios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdaptadorComentarios.ComentarioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_coments, viewGroup, false);
        AdaptadorComentarios.ComentarioViewHolder viewHolder =
                new AdaptadorComentarios.ComentarioViewHolder(itemView, context, listener);
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
        private TextView tvValoracion;
        private CardView comentCard;
        private IComentClickListener listener;


        public ComentarioViewHolder(View itemView, Context context, IComentClickListener listener) {
            super(itemView);
            this.context = context;
            this.listener = listener;

            tvNombre = itemView.findViewById(R.id.tvNameComment);
            tvTimeElapsed = itemView.findViewById(R.id.tvTimeElapsed);
            tvComentario = itemView.findViewById(R.id.tvComment);
            ivFotoPerfil = itemView.findViewById(R.id.ivFotoPerfilComent);
            tvValoracion = itemView.findViewById(R.id.tvValoracioninComments);
            comentCard = itemView.findViewById(R.id.comentCard);


        }

        public void bindPost(final Comentario comentario) {

            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users").document(comentario.getUserUID()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot value = task.getResult();

                            Usuario usuario = value.toObject(Usuario.class);
                            rellenarDatos(usuario, comentario);
                        }
                    });

        }

        public void rellenarDatos(Usuario user, final Comentario comentario) {
            if (user != null) {
                tvNombre.setText(user.getNombre());
                tvComentario.setText(comentario.getComentario());
                String fecha = Lib.getTimeElapsed(comentario.getFecha());
                tvTimeElapsed.setText(fecha);
                tvValoracion.setText(comentario.getValoraciones() + "");


                comentCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {

                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            db.collection("Posts").document(comentario.getPostUID()).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot value = task.getResult();

                                            Post post = value.toObject(Post.class);
                                            listener.comentarioSeleccionado(post);
                                        }
                                    });

                        }
                    }
                });

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


