package com.joelnemi.foro.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.joelnemi.foro.LoadingDialog;
import com.joelnemi.foro.activities.MainActivity;
import com.joelnemi.foro.fragments.FragmentPosts;
import com.joelnemi.foro.listeners.IPerfilClickListener;
import com.joelnemi.foro.models.Post;
import com.joelnemi.foro.R;
import com.joelnemi.foro.models.Usuario;
import com.joelnemi.foro.listeners.IOnClickPostListener;
import com.joelnemi.foro.utils.Lib;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Predicate;


public class AdaptadorPosts extends RecyclerView.Adapter<AdaptadorPosts.PostViewHolder> {
    private ArrayList<Post> posts;
    private Context context;
    private int position;
    private IOnClickPostListener listener;
    private IPerfilClickListener listenerPerfil;


    public AdaptadorPosts(Context context, ArrayList<Post> posts, IOnClickPostListener listener,
                          IPerfilClickListener listenerPerfil) {
        this.context = context;
        this.posts = posts;
        this.listener = listener;
        this.listenerPerfil = listenerPerfil;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);
        PostViewHolder viewHolder = new PostViewHolder(itemView, context, listener, listenerPerfil);
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
        private ImageView ivSave;
        private LinearLayout llPerfil;
        private IPerfilClickListener listenerPerfil;



        public PostViewHolder(View itemView, Context context, IOnClickPostListener listener, IPerfilClickListener listenerPerfil) {
            super(itemView);
            this.context = context;
            this.listener = listener;
            this.listenerPerfil = listenerPerfil;

            tvParrafo = itemView.findViewById(R.id.tvParrafo);
            tvnombrePerfil = itemView.findViewById(R.id.tvNombrePerfil);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            tvnumComs = itemView.findViewById(R.id.tvnumCom);
            tvValoracion = itemView.findViewById(R.id.tvValoracion);
            tvDate = itemView.findViewById(R.id.tvDate);
            bMessage = itemView.findViewById(R.id.ivComments);
            ivUp = itemView.findViewById(R.id.ivUp);
            ivDown = itemView.findViewById(R.id.ivDown);
            ivPhotoProfile = itemView.findViewById(R.id.ivFotoPerfilPost);
            ivFoto = itemView.findViewById(R.id.ivFoto);
            ivSave = itemView.findViewById(R.id.ivSavePost);
            llPerfil = itemView.findViewById(R.id.llPerfil);




        }

        public void bindPost(final Post post) {

            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            LoadingDialog loadingDialog = LoadingDialog.getInstance(null);

            db.collection("users").document(post.getUser()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot value = task.getResult();

                            Usuario usuario = value.toObject(Usuario.class);
                            rellenarDatos(usuario, post);
                            loadingDialog.dissmisDialog();
                        }
                    });

        }

        public void rellenarDatos(Usuario user, final Post post) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            if (user != null) {
                tvParrafo.setText(post.getTexto());
                tvnombrePerfil.setText(user.getNombre());
                tvValoracion.setText(post.getValoracion() + "");
                tvnumComs.setText(post.getComentarios().size() + "");
                tvCategoria.setText(post.getCategoria());


                comprobaciones(user, post);
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

                llPerfil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listenerPerfil.onPerfilSelected(user, context);
                    }
                });

                bMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onUpdateSelected(post);
                    }
                });

                ivSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!user.getIdsPostsGuardados().contains(post.getPostUID())){
                            ArrayList<String> postGuardados = user.getIdsPostsGuardados();
                            postGuardados.add(post.getPostUID());
                            user.setIdsPostsGuardados(postGuardados);
                            ivSave.setImageResource(R.drawable.ic_baseline_save);
                            db.collection("users").document(user.getUserUID()).set(user);

                        }else{
                            ArrayList<String> postGuardados = user.getIdsPostsGuardados();
                            postGuardados.remove(post.getPostUID());
                            user.setIdsPostsGuardados(postGuardados);
                            ivSave.setImageResource(R.drawable.ic_baseline_no_save);
                            db.collection("users").document(user.getUserUID()).set(user);

                        }
                    }
                });

                ivDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!user.getIdsPostsVotadosNegativo().contains(post.getPostUID())&&
                                !user.getIdsPostsVotadosPositivo().contains(post.getPostUID())){
                            votacion(post, v, user);
                        }

                    }
                });
                ivUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!user.getIdsPostsVotadosNegativo().contains(post.getPostUID())&&
                                !user.getIdsPostsVotadosPositivo().contains(post.getPostUID())){
                            votacion(post, v, user);
                        }
                    }
                });
            }

        }

        public void votacion(Post post, View v, Usuario usuario){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            ArrayList<String> postsVotadosP = usuario.getIdsPostsVotadosPositivo();
            ArrayList<String> postsVotadosN = usuario.getIdsPostsVotadosNegativo();
            if (postsVotadosP == null && postsVotadosN == null) {
                postsVotadosP = new ArrayList<>();
                postsVotadosN = new ArrayList<>();
            }
            if (!postsVotadosP.contains(post.getPostUID()) && !postsVotadosN.contains(post.getPostUID())){
                Log.d("votacion",v.getId() + " " + R.id.ivDown);
                switch (v.getId()){
                    case R.id.ivDown:
                        post.setValoracion(post.getValoracion() - 1L);
                        db.collection("Posts").document(post.getPostUID()).set(post);
                        postsVotadosN.add(post.getPostUID());
                        usuario.setIdsPostsVotadosNegativo(postsVotadosN);
                        break;
                    case R.id.ivUp:
                        post.setValoracion(post.getValoracion() + 1L);
                        db.collection("Posts").document(post.getPostUID()).set(post);
                        postsVotadosP.add(post.getPostUID());
                        usuario.setIdsPostsVotadosPositivo(postsVotadosP);
                        break;
                }
                db.collection("users").document(usuario.getUserUID()).set(usuario);
            }

        }

        public void comprobaciones(Usuario user, Post post) {

            if (user.getIdsPostsGuardados().contains(post.getPostUID())) {
                ivSave.setImageResource(R.drawable.ic_baseline_save);
            } else {
                ivSave.setImageResource(R.drawable.ic_baseline_no_save);
            }

            if (user.getIdsPostsVotadosPositivo().contains(post.getPostUID())) {
                ivDown.setColorFilter(ContextCompat.getColor(context, R.color.iconColor));
                ivUp.setColorFilter(ContextCompat.getColor(context, R.color.green));
            }
            if (user.getIdsPostsVotadosNegativo().contains(post.getPostUID())) {
                ivUp.setColorFilter(ContextCompat.getColor(context, R.color.iconColor));
                ivDown.setColorFilter(ContextCompat.getColor(context, R.color.red));

            }

        }

    }
}


