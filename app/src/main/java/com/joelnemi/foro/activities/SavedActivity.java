package com.joelnemi.foro.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.joelnemi.foro.LoadingDialog;
import com.joelnemi.foro.adapters.AdaptadorComentarios;
import com.joelnemi.foro.adapters.AdaptadorOpciones;
import com.joelnemi.foro.adapters.AdaptadorPosts;
import com.joelnemi.foro.fragments.FragmentComments;
import com.joelnemi.foro.listeners.IOnClickPostListener;
import com.joelnemi.foro.models.Comentario;
import com.joelnemi.foro.models.Post;
import com.joelnemi.foro.R;
import com.joelnemi.foro.models.Usuario;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class SavedActivity extends AppCompatActivity implements IOnClickPostListener {


    private RecyclerView rvListado;
    private AdaptadorPosts adaptador;
    private Usuario usuario;

    private ArrayList<Post> postsGuardados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardados);


        Toolbar toolbar = findViewById(R.id.toolbar_postsGuardados);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                usuario = null;
            } else {
                usuario = (Usuario) extras.getSerializable("usuario");
            }
        } else {
            usuario = (Usuario) savedInstanceState.getSerializable("usuario");
        }


        if (toolbar != null) {
            setSupportActionBar(toolbar);


            final ActionBar actionBar = getSupportActionBar();

            if (actionBar != null) {
                actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
                actionBar.setTitle("Posts Guardados");
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }


        rvListado = findViewById(R.id.rvItemPosts);
        postsGuardados = new ArrayList<>();

        buscarPostGuardados();


    }


    public void buscarPostGuardados() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        for (int i = 0; i < usuario.getIdsPostsGuardados().size(); i++) {

            int finalI = i;
            db.collection("Posts").document(usuario.getIdsPostsGuardados().get(i)).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot value = task.getResult();

                            postsGuardados.add(value.toObject(Post.class));

                            if (finalI == usuario.getIdsPostsGuardados().size() - 1) {
                                adaptador = new AdaptadorPosts(SavedActivity.this, postsGuardados,
                                        SavedActivity.this);
                                rvListado.setAdapter(adaptador);

                                rvListado.setLayoutManager(new LinearLayoutManager(
                                        SavedActivity.this, LinearLayoutManager.VERTICAL, false));

                                rvListado.addItemDecoration(new DividerItemDecoration(
                                        rvListado.getContext(), DividerItemDecoration.VERTICAL));

                            }

                        }
                    });
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    public void onUpdateSelected(Post post) {
        Intent i = new Intent(this, DetalleActivity.class);
        i.putExtra("post", post);
        startActivity(i);
    }


}