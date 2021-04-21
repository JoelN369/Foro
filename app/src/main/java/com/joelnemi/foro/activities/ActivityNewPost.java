package com.joelnemi.foro.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.joelnemi.foro.LoadingDialog;
import com.joelnemi.foro.adapters.AdaptadorOpciones;
import com.joelnemi.foro.models.Comentario;
import com.joelnemi.foro.models.Post;
import com.joelnemi.foro.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ActivityNewPost extends AppCompatActivity implements View.OnClickListener{

    private static final int UPLOAD_IMAGE = 1001;


    private ImageButton ibAddImage;
    private boolean valido;
    private String userUID;

    private Spinner spCat;

    private EditText etTexto;
    private ArrayList<String> categorias;
    private String userPhoto;
    private String categoria;
    private Uri uri;
    private boolean fotoSubida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);


        //Inicializo los atributos
        Toolbar toolbar = findViewById(R.id.toolbar_post);
        spCat = findViewById(R.id.spCat);

        etTexto = findViewById(R.id.etNewTitulo);
        ibAddImage = findViewById(R.id.ibAddFoto);
        ibAddImage.setOnClickListener(this);
        categorias = new ArrayList<>();
        llenarSpinner();
        fotoSubida = false;


        //Compruebo de que tenga el Bundle y guardo el userUID en una variable
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                userUID = null;
            } else {
                userUID = extras.getString("userID");
            }
        } else {
            userUID = savedInstanceState.getString("userID");
        }

        //Asigno la toolbar, le asigno el titulo y el icono para volver atras
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();

            if (actionBar != null) {
                actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
                actionBar.setTitle("Upload post");
                actionBar.setDisplayHomeAsUpEnabled(true);

            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation,menu);

        return true;
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
     * Metodo onClick de la activity
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Muestro la galeria y le permito al usuario añadir una imagen al Post
            case R.id.ibAddFoto:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), UPLOAD_IMAGE);
                break;

            default:

        }
    }

    /**
     * Metodo para las opciones de la toolbar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            //Cuando la foto este subida podra subir el post
            case R.id.navigation_post:
                if (!fotoSubida) {
                    subirfoto(uri);
                    return false;
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void guardarPost(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //No puede subir un post si no ha escrito nada
        if (!(etTexto.getText().equals("")||etTexto.getText() == null)) {

            if (fotoSubida && userUID != null) {
                ArrayList<Comentario> comentarios = new ArrayList<>();

                String postUID = UUID.randomUUID() + "";

                db.collection("Posts").document(postUID).set(new Post(postUID, etTexto.getText().toString(), userPhoto, 0L,
                        comentarios, userUID, categoria, new Date())).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        onBackPressed();
                    }
                });

            } else {
                Toast.makeText(this, "Problema con reconocer el Usuario", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Se necesita añadir texto al Post", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Relleno el Spinner con las categorias de la BBDD
     */
    public void llenarSpinner() {

        getCategorias();

        spCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoria = (String) parent.getItemAtPosition(position);
            }

            //Predeterminadamente asigno el primer valor por defecto
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categoria = (String) parent.getItemAtPosition(0);
            }
        });
    }

    /**
     * Busco en la BBDD las categorias y se las asigno al Spinner
     */
    public void getCategorias() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("categorias").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable  QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                Log.d("cat",value.getDocuments().get(0).getData().get("nombre").toString());

                for (DocumentSnapshot ds :value.getDocuments()){
                    categorias.add(ds.getData().get("nombre").toString());
                }

                ArrayAdapter<String> adapterCategoria = new AdaptadorOpciones(getApplicationContext(),
                        android.R.layout.simple_dropdown_item_1line, categorias);
                adapterCategoria.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

                spCat.setAdapter(adapterCategoria);
                spCat.setSelection(0);
            }
        });

    }

    /**
     * Cuando haya asignado la foto que desea subir en el post viene a este metodo y la sube a firebase
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == UPLOAD_IMAGE && resultCode == Activity.RESULT_OK){
            if (data!=null){
                ibAddImage.setImageURI(data.getData());
                ibAddImage.setColorFilter(Color.TRANSPARENT);
                uri = data.getData();

            }else {
                userPhoto = "";
            }
        }
    }

    /**
     * Sube la foto a firebase
     * @param uri
     */
    public void subirfoto(Uri uri){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        final StorageReference ref = storageRef.child("images/"+ UUID.randomUUID() +".jpg");

        //Muestra un dialogo como que esta cargando hasta que se suba la imagen
        LoadingDialog loadingDialog = LoadingDialog.getInstance(this);
        loadingDialog.changeTextDialog("Subiendo Foto...");
        loadingDialog.startDialog();
        ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        userPhoto = uri.toString();
                        fotoSubida = true;
                        loadingDialog.dissmisDialog();
                        guardarPost();
                    }
                });
            }
        });

    }
}

