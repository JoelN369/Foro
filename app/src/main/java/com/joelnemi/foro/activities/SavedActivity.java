package com.joelnemi.foro.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;
import com.joelnemi.foro.adapters.AdaptadorPosts;
import com.joelnemi.foro.listeners.IOnClickPostListener;
import com.joelnemi.foro.models.Post;
import com.joelnemi.foro.R;
import com.joelnemi.foro.models.Usuario;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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

        //Compruebo de que tenga el Bundle y guardo el userUID en una variable
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

        //Asigno la toolbar, le asigno el titulo y el icono para volver atras
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

    /**
     * Busca los post guardados del usuario registrado
     */
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

    /**
     * Cuando le da al icono de la toolbar vuelve atras
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    /**
     * Cuando el usuario le da click al post y se muestra el detalle
     * @param post
     */
    @Override
    public void onUpdateSelected(Post post) {
        Intent i = new Intent(this, DetalleActivity.class);
        i.putExtra("post", post);
        startActivity(i);
    }


}