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
import com.joelnemi.foro.*;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeFragment extends Fragment implements IOnClickPostListener {

    private static HomeFragment fragment;



    private RecyclerView rvListado;
    private AdaptadorPosts adaptador;

    private static ArrayList<Post> posts;

    private HomeFragment() {
    }

    public static HomeFragment getInstance(ArrayList<Post> post) {
        if (fragment == null) {
            fragment = new HomeFragment();
            posts = post;
        }

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

        rvListado = v.findViewById(R.id.rvItemPosts);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adaptador = new AdaptadorPosts(getContext(), posts,this);
        rvListado.setAdapter(adaptador);
        rvListado.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvListado.addItemDecoration(new DividerItemDecoration(rvListado.getContext(), DividerItemDecoration.VERTICAL));

    }

    @Override
    public void onUpdateSelected(Post post) {
        Intent i = new Intent(getContext(), DetailFragment.class);
        i.putExtra("post",post);
        startActivity(i);
    }
}