package com.joelnemi.foro.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.joelnemi.foro.*;
import com.joelnemi.foro.adapters.AdaptadorComentarios;
import com.joelnemi.foro.models.Comentario;
import com.joelnemi.foro.models.Post;
import com.joelnemi.foro.models.Usuario;
import com.joelnemi.foro.utils.Lib;

import java.util.ArrayList;
import java.util.Date;

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
    private static Post post;
    private RecyclerView rvComments;
    private AdaptadorComentarios adaptador;
    private ImageButton ibSendComment;
    private EditText etCommentText;
    private NestedScrollView svDetalle;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        final Toolbar toolbar = findViewById(R.id.toolBarDetail);

        final NestedScrollView scrollView = findViewById(R.id.svDetalle);

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


        //Compruebo si el bundle es nulo y si contiene extras y las guardo
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                post = null;
            } else {
                post= (Post) extras.getSerializable("post");
            }
        } else {
            post= (Post) savedInstanceState.getSerializable("post");
        }

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(post.getUser()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.getData() == null) {

                } else {

                    Usuario usuario = value.toObject(Usuario.class);
                    rellenarDatos(usuario,post);
                    modificarToolbar(toolbar, scrollView, usuario);
                }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    /**
     * Modifica la toolbar mostrando el nombre de usuario que subio este post
     * @param toolbar
     * @param scrollView
     * @param usuario
     */
    public void modificarToolbar(final Toolbar toolbar, NestedScrollView scrollView, final Usuario usuario){
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                //Si la pantalla se desplaza mas de 200 pixeles se muestra el nombre del usuario
                if (scrollY>200) {


                    if (toolbar != null) {

                        final ActionBar actionBar = getSupportActionBar();
                        if (actionBar != null) {
                            actionBar.setTitle(usuario.getNombre());

                        }
                    }
                }else{
                    if (toolbar != null) {
                        final ActionBar actionBar = getSupportActionBar();

                        if (actionBar != null) {
                            actionBar.setTitle("");
                        }
                    }
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
            db = FirebaseFirestore.getInstance();

            if (post.getUrlFoto() != null)
                if (!post.getUrlFoto().equals(""))
                    Glide.with(this)
                            .load(post.getUrlFoto())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .thumbnail(0.1f)
                            .into(ivFoto);

            if (user.getFoto() != null)
                if (!user.getFoto().equals(""))
                    Glide.with(this)
                            .load(user.getFoto())
                            .apply(RequestOptions.circleCropTransform())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .thumbnail(0.1f)
                            .into(ivPhotoProfile);


            String fecha = Lib.getTimeElapsed(post.getFechaPost());

            tvDate.setText(fecha);

            ivDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivUp.setColorFilter(ContextCompat.getColor(DetalleActivity.this, R.color.iconColor));
                    ivDown.setColorFilter(ContextCompat.getColor(DetalleActivity.this, R.color.red));


                }
            });
            ivUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ivDown.setColorFilter(ContextCompat.getColor(DetalleActivity.this, R.color.iconColor));
                    ivUp.setColorFilter(ContextCompat.getColor(DetalleActivity.this, R.color.green));

                }
            });

            ibSendComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comentario = etCommentText.getText().toString();
                    if (!comentario.equals("")) {
                        post.getComentarios().add(new Comentario(user.getUserUID(), comentario,
                                new Date(), 0L));


                        crearReciclerView(post.getComentarios());
                        db.collection("Posts").document(post.getPostUID()).update("comentarios", post.getComentarios());

                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        svDetalle.fullScroll(NestedScrollView.FOCUS_DOWN);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        etCommentText.setText("");
                    }
                }
            });

            crearReciclerView(post.getComentarios());
        }
    }

    public void crearReciclerView(ArrayList<Comentario> comentarios){
        adaptador = new AdaptadorComentarios(this, comentarios);
        rvComments.setAdapter(adaptador);
        rvComments.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvComments.addItemDecoration(new DividerItemDecoration(rvComments.getContext(), DividerItemDecoration.VERTICAL));
    }
}