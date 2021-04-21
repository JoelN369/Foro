package com.joelnemi.foro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.joelnemi.foro.*;
import com.joelnemi.foro.activities.DetalleActivity;
import com.joelnemi.foro.adapters.AdaptadorPosts;
import com.joelnemi.foro.listeners.IOnClickPostListener;
import com.joelnemi.foro.listeners.IPerfilClickListener;
import com.joelnemi.foro.listeners.IRefreshListener;
import com.joelnemi.foro.models.Post;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements IOnClickPostListener {

    private static HomeFragment fragment;



    private RecyclerView rvListado;
    private AdaptadorPosts adaptador;
    private SwipeRefreshLayout srlHome;
    private static IRefreshListener listener;
    private static IPerfilClickListener listenerPerfil;


    private static ArrayList<Post> posts;

    private HomeFragment() {
    }

    public static HomeFragment getInstance(ArrayList<Post> post, IRefreshListener listenerP,
                                           IPerfilClickListener listenerPerfil1) {
        if (fragment == null) {
            fragment = new HomeFragment();
            listener = listenerP;
            listenerPerfil = listenerPerfil1;

        }

        posts = post;
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //Inicializo los componentes graficos
        rvListado = v.findViewById(R.id.rvItemPosts);
        srlHome = v.findViewById(R.id.srlHome);
        srlHome.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, android.R.color.holo_green_light);
        srlHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listener.onRefresh();

            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Creo el recicler view con los posts
        adaptador = new AdaptadorPosts(getContext(), posts,this, listenerPerfil);
        rvListado.setAdapter(adaptador);
        rvListado.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvListado.addItemDecoration(new DividerItemDecoration(rvListado.getContext(), DividerItemDecoration.VERTICAL));

    }

    /**
     * Cuando seleccione un post entrara en el detalle
     * @param post
     */
    @Override
    public void onUpdateSelected(Post post) {
        Intent i = new Intent(getContext(), DetalleActivity.class);
        i.putExtra("post",post);
        i.putExtra("listener", listenerPerfil);
        startActivity(i);
    }
}