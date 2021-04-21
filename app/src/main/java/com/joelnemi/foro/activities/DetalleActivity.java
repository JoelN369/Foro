package com.joelnemi.foro.activities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.joelnemi.foro.*;
import com.joelnemi.foro.adapters.AdaptadorComentarios;
import com.joelnemi.foro.listeners.IPerfilClickListener;
import com.joelnemi.foro.models.Comentario;
import com.joelnemi.foro.models.Post;
import com.joelnemi.foro.models.Usuario;
import com.joelnemi.foro.utils.Lib;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Predicate;

public class DetalleActivity extends AppCompatActivity {

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
    private static Post post;
    private RecyclerView rvComments;
    private AdaptadorComentarios adaptador;
    private ImageButton ibSendComment;
    private EditText etCommentText;
    private NestedScrollView svDetalle;
    private IPerfilClickListener listenerPerfil;
    private LinearLayout llPerfil;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);


        final Toolbar toolbar = findViewById(R.id.toolBarDetail);
        final NestedScrollView scrollView = findViewById(R.id.svDetalle);


        //Asigno la toolbar, le asigno el titulo y el icono para volver atras
        if (toolbar != null) {
            setSupportActionBar(toolbar);


            final ActionBar actionBar = getSupportActionBar();

            if (actionBar != null) {
                actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle("");

            }
        }


        //Inicializo los components graficos
        tvParrafo = findViewById(R.id.tvParrafo);
        tvnombrePerfil = findViewById(R.id.tvNombrePerfil);
        tvCategoria = findViewById(R.id.tvCategoria);
        tvnumComs = findViewById(R.id.tvnumCom);
        tvValoracion = findViewById(R.id.tvValoracion);
        tvDate = findViewById(R.id.tvDate);
        bMessage = findViewById(R.id.ivComments);
        ivUp = findViewById(R.id.ivUp);
        ivDown = findViewById(R.id.ivDown);
        ivPhotoProfile = findViewById(R.id.ivFotoPerfilPost);
        ivFoto = findViewById(R.id.ivFoto);
        rvComments = findViewById(R.id.rvComments);
        ibSendComment = findViewById(R.id.ibSendComment);
        etCommentText = findViewById(R.id.etCommentText);
        svDetalle = findViewById(R.id.svDetalle);
        ivSave = findViewById(R.id.ivSavePost);
        llPerfil = findViewById(R.id.llPerfil);




        //Compruebo si el bundle es nulo y si contiene extras y las guardo
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                post = null;
            } else {
                listenerPerfil = (IPerfilClickListener) extras.getSerializable("listener");
                post = (Post) extras.getSerializable("post");
            }
        } else {
            listenerPerfil = (IPerfilClickListener) savedInstanceState.getSerializable("listener");
            post = (Post) savedInstanceState.getSerializable("post");
        }

        final FirebaseFirestore db = FirebaseFirestore.getInstance();


        //Busco al usuario y relleno los datos del post
        db.collection("users").document(post.getUser()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                DocumentSnapshot value = task.getResult();

                Usuario usuario = value.toObject(Usuario.class);
                rellenarDatos(usuario, post);
                modificarToolbar(scrollView, usuario);
            }
        });



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    /**
     * Modifica la toolbar mostrando el nombre de usuario que subio este post cuando el usuario desplazo
     * hacia abajo mas de 200 pixeles
     *
     * @param usuario
     */
    public void modificarToolbar(NestedScrollView scrollView, Usuario usuario) {
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                //Si la pantalla se desplaza mas de 200 pixeles se muestra el nombre del usuario
                if (scrollY > 200) {

                    final ActionBar actionBar = getSupportActionBar();

                    if (actionBar != null && actionBar.getTitle().equals("")) {
                        actionBar.setTitle(usuario.getNombre());
                    }

                } else {
                    final ActionBar actionBar = getSupportActionBar();

                    if (actionBar != null && actionBar.getTitle().equals(usuario.getNombre())) {
                        actionBar.setTitle("");

                    }
                }
            }
        });


    }

    /**
     * Rellena los datos del post y sus comentarios con la informacion del usuario y del post
     * @param user el usuario que subio el post
     * @param post El post
     */
    public void rellenarDatos(Usuario user, final Post post) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (user != null) {
            tvParrafo.setText(post.getTexto());
            tvnombrePerfil.setText(user.getNombre());
            tvValoracion.setText(post.getValoracion() + "");
            tvnumComs.setText(post.getComentarios().size() + "");
            tvCategoria.setText(post.getCategoria());

            comprobaciones(user, post);

            //Asigno la foto del post y del usuario
            if (post.getUrlFoto() != null)
                if (!post.getUrlFoto().equals(""))
                    Glide.with(DetalleActivity.this)
                            .load(post.getUrlFoto())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .thumbnail(0.1f)
                            .into(ivFoto);
            if (user.getFoto() != null)
                if (!user.getFoto().equals(""))
                    Glide.with(DetalleActivity.this)
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
                    listenerPerfil.onPerfilSelected(user,DetalleActivity.this);
                }
            });

            //Boton para guardar el post
            ivSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!user.getIdsPostsGuardados().contains(post.getPostUID())){
                        ArrayList<String> postGuardados = user.getIdsPostsGuardados();
                        postGuardados.add(post.getPostUID());
                        user.setIdsPostsGuardados(postGuardados);
                        db.collection("users").document(user.getUserUID()).set(user);

                    }else{
                        ArrayList<String> postGuardados = user.getIdsPostsGuardados();
                        postGuardados.removeIf(Predicate.isEqual(post.getPostUID()));
                        user.setIdsPostsGuardados(postGuardados);
                        db.collection("users").document(user.getUserUID()).set(user);

                    }
                }
            });

            //Boton para votar negativamente el post
            ivDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!user.getIdsPostsVotadosNegativo().contains(post.getPostUID())&&
                            !user.getIdsPostsVotadosPositivo().contains(post.getPostUID())){
                        votacion(post, v, user);
                        ivUp.setColorFilter(ContextCompat.getColor(DetalleActivity.this, R.color.iconColor));
                        ivDown.setColorFilter(ContextCompat.getColor(DetalleActivity.this, R.color.red));
                    }

                }
            });

            //Boton para votar positivamente el post cambia de color el boton de votar
            ivUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!user.getIdsPostsVotadosNegativo().contains(post.getPostUID())&&
                            !user.getIdsPostsVotadosPositivo().contains(post.getPostUID())){
                        votacion(post, v, user);
                        ivDown.setColorFilter(ContextCompat.getColor(DetalleActivity.this, R.color.iconColor));
                        ivUp.setColorFilter(ContextCompat.getColor(DetalleActivity.this, R.color.green));
                    }
                }
            });

            //Boton para enviar el comentario escrito al post
            ibSendComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comentario = etCommentText.getText().toString();
                    if (!comentario.equals("")) {

                        //guardo el comentario
                        Comentario comentario1 = new Comentario(user.getUserUID(), comentario,
                                new Date(), 0L,post.getPostUID());
                        post.getComentarios().add(comentario1);

                        //El comentario se añade al reciclerView
                        crearReciclerView(post.getComentarios());
                        db.collection("Posts").document(post.getPostUID()).update("comentarios",
                                post.getComentarios());


                        //Comprueba si el usuario tiene el arraylist de post guardados vacio
                        if (user.getComentariosPublicados() == null) {
                            ArrayList<Comentario> comentarios = new ArrayList<>();
                            comentarios.add(comentario1);
                            user.setComentariosPublicados(comentarios);
                        }else{
                            user.getComentariosPublicados().add(comentario1);
                        }



                        db.collection("users").document(user.getUserUID()).set(user);

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        svDetalle.fullScroll(NestedScrollView.FOCUS_DOWN);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        etCommentText.setText("");
                    }
                }
            });

            crearReciclerView(post.getComentarios());
        }

    }

    /**
     * Segun el boton al que le des se añadira a un array o a otro
     * @param post El post que esta valorando
     * @param v El boton al que le das click
     * @param usuario El usuario que esta votando
     */
    public void votacion(Post post, View v, Usuario usuario){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<String> postsVotadosP = usuario.getIdsPostsVotadosPositivo();
        ArrayList<String> postsVotadosN = usuario.getIdsPostsVotadosNegativo();

        //si el usuario tiene alguno de los Arrays vacios se crea
        if (postsVotadosP == null ) {
            postsVotadosP = new ArrayList<>();
        }
        if (postsVotadosN == null) {
            postsVotadosN = new ArrayList<>();
        }
        //Si el post no esta en ningun arraylist se añade a su array coorrespondiente
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


    //Comprueba si el usuario tiene los post guardados o votados y modifica componentes visuales
    public void comprobaciones(Usuario user, Post post) {

        if (user.getIdsPostsGuardados().contains(post.getPostUID())) {
            ivSave.setImageResource(R.drawable.ic_baseline_save);
        } else {
            ivSave.setImageResource(R.drawable.ic_baseline_no_save);
        }

        if (user.getIdsPostsVotadosPositivo().contains(post.getPostUID())) {
            ivDown.setColorFilter(ContextCompat.getColor(DetalleActivity.this, R.color.iconColor));
            ivUp.setColorFilter(ContextCompat.getColor(DetalleActivity.this, R.color.green));
        }
        if (user.getIdsPostsVotadosNegativo().contains(post.getPostUID())) {
            ivUp.setColorFilter(ContextCompat.getColor(DetalleActivity.this, R.color.iconColor));
            ivDown.setColorFilter(ContextCompat.getColor(DetalleActivity.this, R.color.red));

        }

    }

    /**
     * Creo el recicler View con los comentarios
     * @param comentarios los comentarios
     */
    public void crearReciclerView(ArrayList<Comentario> comentarios) {
        adaptador = new AdaptadorComentarios(this, comentarios,null);
        rvComments.setAdapter(adaptador);
        rvComments.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvComments.addItemDecoration(new DividerItemDecoration(rvComments.getContext(), DividerItemDecoration.VERTICAL));
    }
}