package com.joelnemi.foro.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joelnemi.foro.*;
import com.joelnemi.foro.adapters.FragmentAdapter;
import com.joelnemi.foro.listeners.IComentClickListener;
import com.joelnemi.foro.listeners.IPerfilClickListener;
import com.joelnemi.foro.models.Comentario;
import com.joelnemi.foro.models.Post;
import com.joelnemi.foro.models.Usuario;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {


    private static Usuario usuario;

    private TextView tvNameUser;
    private ImageView ivUserPhoto;
    private ViewPager mViewPager;
    private FragmentAdapter adapter;
    private IPerfilClickListener listenerPerfil;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Compruebo de que tenga el Bundle y guardo el userUID en una variable
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {

            } else {
                usuario = (Usuario) extras.getSerializable("usuario");
                listenerPerfil = (IPerfilClickListener) extras.getSerializable("listener");
            }
        } else {
            usuario = (Usuario) savedInstanceState.getSerializable("usuario");
            listenerPerfil = (IPerfilClickListener) savedInstanceState.getSerializable("listener");
        }

        final Toolbar toolbar = findViewById(R.id.toolbarProfile);
        final CollapsingToolbarLayout ctToolbar = findViewById(R.id.ctToolbarProfile);
        final AppBarLayout appBarLayout = findViewById(R.id.appbarProfile);


        //compruebo el desplazamiento del appbar layout y muestro o no el nombre  de usuario
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                double percentage = (double) Math.abs(verticalOffset) / ctToolbar.getHeight();
                if (percentage > 0.8) {
                    ctToolbar.setTitle(usuario.getNombre());
                } else {
                    ctToolbar.setTitle("");
                }
            }
        });


        //Asigno la toolbar, le asigno el titulo y el icono para volver atras
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            if (usuario != null) {
                toolbar.setTitle(usuario.getNombre());
            }
            final ActionBar actionBar = getSupportActionBar();

            if (actionBar != null) {
                actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle("");

            }
        }

        //Inicializo los components graficos
        ivUserPhoto = findViewById(R.id.ivFotoPerfil);
        tvNameUser = findViewById(R.id.tvName);
        adapter = new FragmentAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,this, listenerPerfil);
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new
                TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        tvNameUser.setText(usuario.getNombre());
        String url = usuario.getFoto().toString();
        if (usuario.getFoto() != null) {
            Glide.with(getBaseContext())
                    .load(url)
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(ivUserPhoto);
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
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


}