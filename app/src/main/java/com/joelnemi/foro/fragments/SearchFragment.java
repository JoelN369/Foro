package com.joelnemi.foro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.*;
import com.joelnemi.foro.R;
import com.joelnemi.foro.activities.DetalleActivity;
import com.joelnemi.foro.adapters.AdaptadorOpciones;
import com.joelnemi.foro.adapters.AdaptadorPosts;
import com.joelnemi.foro.listeners.IOnClickPostListener;
import com.joelnemi.foro.listeners.IRefreshListener;
import com.joelnemi.foro.models.Post;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchFragment extends Fragment implements IOnClickPostListener {

    private static SearchFragment fragment;

    private RecyclerView rvListado;
    private AdaptadorPosts adaptador;
    private SwipeRefreshLayout srlHome;
    private static IRefreshListener listener;
    private Spinner spOrder;

    private static ArrayList<Post> posts;
    private static final String[] OPCIONES_SPINNER = {"Recientes", "Más Valorados","Hot"};
    private static final String[] OPCIONES_ORDER = {"fechaPost", "valoracion","valoracion"};




    public static SearchFragment getInstance(ArrayList<Post> post) {
        if (fragment == null) {
            fragment = new SearchFragment();


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
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        rvListado = v.findViewById(R.id.rvItemPosts);
        spOrder = v.findViewById(R.id.spOrden);

        llenarSpinner();

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
        Intent i = new Intent(getContext(), DetalleActivity.class);
        i.putExtra("post",post);
        startActivity(i);
    }

    public void llenarSpinner() {



        ArrayList<String> opciones =
                new ArrayList<String>(Arrays.asList(OPCIONES_SPINNER));

        ArrayAdapter<String> adapterCategoria = new AdaptadorOpciones(getContext(),
                android.R.layout.simple_dropdown_item_1line, opciones);
        adapterCategoria.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        spOrder.setAdapter(adapterCategoria);
        spOrder.setSelection(0);


        spOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                actualizarListado(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                actualizarListado(0);
            }
        });
    }

    public void actualizarListado(int position){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Posts").orderBy(OPCIONES_ORDER[position], Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                posts = (ArrayList<Post>) value.toObjects(Post.class);
                Log.d("postjoel", value.getDocuments().toString());

                adaptador = new AdaptadorPosts(getContext(), posts,SearchFragment.this);
                rvListado.setAdapter(adaptador);
                rvListado.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                rvListado.addItemDecoration(new DividerItemDecoration(rvListado.getContext(), DividerItemDecoration.VERTICAL));

            }
        });




    }

}