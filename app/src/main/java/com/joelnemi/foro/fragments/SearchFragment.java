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
import com.joelnemi.foro.listeners.IPerfilClickListener;
import com.joelnemi.foro.listeners.IRefreshListener;
import com.joelnemi.foro.models.Post;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchFragment extends Fragment implements IOnClickPostListener {

    private static SearchFragment fragment;

    private RecyclerView rvListado;
    private AdaptadorPosts adaptador;
    private Spinner spOrder;

    private static ArrayList<Post> posts;
    private static IPerfilClickListener listenerPerfil;

    //Estos 2 arrays estan ligados, el primero muestra al usuario como ordenar los post mostrados
    // y el segundo es la palabra del atributo de la clase por la que se ordena en firebase
    private static final String[] OPCIONES_SPINNER = {"Recientes", "Más Valorados","Hot"};
    private static final String[] OPCIONES_ORDER = {"fechaPost", "valoracion", "valoracion"};


    /**
     * Guardo una Instancia para que cuando se mueva por el menu de abajo no se creen
     * instancias cada vez que cambia de fragment
     * @param post Los post que se mostraran en el Recicler
     * @return El fragment
     */
    public static SearchFragment getInstance(ArrayList<Post> post, IPerfilClickListener listenerP) {
        if (fragment == null) {
            fragment = new SearchFragment();
            listenerPerfil = listenerP;

        }

        posts = post;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        //Inicializo los componentes visuales
        rvListado = v.findViewById(R.id.rvItemPosts);
        spOrder = v.findViewById(R.id.spOrden);

        //Lleno el spinner con las opciones
        llenarSpinner();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        i.putExtra("listener",listenerPerfil);
        startActivity(i);
    }


    /**
     * LLeno los valores al spinner y le asigno un listener
     */
    public void llenarSpinner() {

        ArrayList<String> opciones =
                new ArrayList(Arrays.asList(OPCIONES_SPINNER));

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


    /**
     * Cuando le de a alguna opcion del spinner enviara la posicion a este metodo y
     * mostrara el listado de post ordenados segun la opcion del spinner
     * @param position
     */
    public void actualizarListado(int position){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Posts").orderBy(OPCIONES_ORDER[position], Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                posts = (ArrayList<Post>) value.toObjects(Post.class);
                Log.d("postjoel", value.getDocuments().toString());

                adaptador = new AdaptadorPosts(getContext(), posts,SearchFragment.this,listenerPerfil);
                rvListado.setAdapter(adaptador);
                rvListado.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                rvListado.addItemDecoration(new DividerItemDecoration(rvListado.getContext(), DividerItemDecoration.VERTICAL));

            }
        });

    }

}