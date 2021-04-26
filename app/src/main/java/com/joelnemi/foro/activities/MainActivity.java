package com.joelnemi.foro.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.EventListener;
import com.joelnemi.foro.CrearComunidadDialog;
import com.joelnemi.foro.LoadingDialog;
import com.joelnemi.foro.R;
import com.joelnemi.foro.fragments.HomeFragment;
import com.joelnemi.foro.fragments.SearchFragment;
import com.joelnemi.foro.listeners.IRefreshListener;
import com.joelnemi.foro.listeners.InstancePerfilListener;
import com.joelnemi.foro.models.Post;
import com.joelnemi.foro.models.Usuario;
import org.jetbrains.annotations.NotNull;


import java.util.*;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener, IRefreshListener {

    private BottomNavigationView bottomNavigation;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private ArrayList<Post> posts;
    private String userUID;
    private Usuario usuario;
    private boolean isMainActivityRunnning;
    private InstancePerfilListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Login con Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Descargo los post y los muestro en la pestaña principal
        descargarDatos();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        mAuth = FirebaseAuth.getInstance();

        //Cargo el navigation y la toolbar
        cargarNavigation();


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Cuando vuelven a esta activity desde otra el booleano se vuelve true y se selecciona en el menu
        // de abajo la pestaña Home
        isMainActivityRunnning = true;
        if (bottomNavigation.getSelectedItemId() != R.id.navigation_home) {
            bottomNavigation.setSelectedItemId(R.id.navigation_home);
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        //Cuando se navega a otra activity este el booleano se vuelve false
        //Este booleano sirve para control de errores al ejecutarse el listener de descargar los datos
        isMainActivityRunnning = false;
    }

    /**
     * Sign in con Google
     *
     * @param idToken
     */
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }


    /**
     * Este metodo descarga los post de la BBDD de Firebase mostrando los primeros los mas recientes
     */
    public void descargarDatos() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Muestro un dialogo mientras se descargan los Posts
        LoadingDialog loadingDialog = LoadingDialog.getInstance(MainActivity.this);
        loadingDialog.startDialog();

        db.collection("Posts").orderBy("fechaPost", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                //Guardo los post en un Array List para pasarlo a un Fragment
                posts = (ArrayList<Post>) value.toObjects(Post.class);

                //Si estamos en main activity y se ejecuta en el fragment Home
                if (isMainActivityRunnning) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.flMainLayout,
                                    HomeFragment.getInstance(posts,
                                            MainActivity.this, listener)).
                            addToBackStack(null)
                            .commit();
                    bottomNavigation.setSelectedItemId(R.id.navigation_home);


                }
            }
        });



    }


    /**
     * Comprueba si el usuario registrado esta en la BBDD si no lo esta crea un nuevo usuario y si esta
     * lo guarda y actualiza los datos del perfil con su nombre y foto
     *
     * @param user El usuario registrado o logeado
     */
    public void updateUI(final FirebaseUser user) {
        userUID = user.getUid();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        //DocumentReference docRef = db.collection("users").document(userUID).set(new Usuario());
        db.collection("users").document(userUID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.getData() == null) {
                    usuario = new Usuario(user.getUid(), user.getDisplayName(), user.getPhotoUrl().toString());
                    db.collection("users").document(userUID).set(usuario);
                } else {
                    usuario = value.toObject(Usuario.class);
                }
            }
        });

        // inicializo las variables
        ImageView ivFoto = findViewById(R.id.ivFotoPerfil);
        TextView tvNombre = findViewById(R.id.tvName);

        //Escribo el nombre
        tvNombre.setText(user.getDisplayName());


        //Añado la imagen
        String url = user.getPhotoUrl().toString();
        if (user.getPhotoUrl() != null) {
            Glide.with(getBaseContext())
                    .load(url)
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(ivFoto);
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //En el menu de abajo de la aplicacion segun la opcion que le de el usuario se
        // ejecutara un fragment o un Activity

        switch (item.getItemId()) {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flMainLayout, HomeFragment.getInstance(posts,
                                MainActivity.this, listener))
                        .addToBackStack(null)
                        .commit();

                return true;
            case R.id.navigation_search:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flMainLayout, SearchFragment.getInstance(posts, listener)).addToBackStack(null)
                        .addToBackStack(null)
                        .commit();

                return true;
            case R.id.navigation_upload:
                Intent i = new Intent(MainActivity.this, ActivityNewPost.class);
                i.putExtra("userID", userUID);
                startActivity(i);
                return true;
            case R.id.navigation_messages:
                return true;
            case R.id.navigation_profile:
                Intent iProfile = new Intent(MainActivity.this, ProfileActivity.class);
                iProfile.putExtra("usuario", usuario);
                iProfile.putExtra("listener", listener);
                startActivity(iProfile);
                return true;
        }
        return false;
    }

    /**
     * Cargo el navigation y la toolbar
     */
    public void cargarNavigation() {

        listener = InstancePerfilListener.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_saved:
                        Intent iProfile = new Intent(MainActivity.this, SavedActivity.class);
                        iProfile.putExtra("usuario", usuario);
                        startActivity(iProfile);
                        return true;
                    case R.id.nav_history:
                        //Llevar a una Activity con los post valorados o guardados recientemente
                        return true;
                    case R.id.nav_comunidad:
                        // Mostrar un dialogo para crear una nueva comunidad
                        CrearComunidadDialog dialog = new CrearComunidadDialog(MainActivity.this);
                        dialog.startDialog();

                        return true;
                }
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        bottomNavigation.setOnNavigationItemSelectedListener(this);


    }


    /**
     * Cuando se arrastra hacia arriba para recargar los posts
     */
    @Override
    public void onRefresh() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Posts").orderBy("fechaPost", Query.Direction.DESCENDING).get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                        //Guarfdo los post en un Array List para pasarlo a un Fragment
                        posts = (ArrayList<Post>) task.getResult().toObjects(Post.class);


                        //Si estamos en main activity y se ejecuta en el fragment Home
                        if (isMainActivityRunnning) {
                            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flMainLayout);
                            if (currentFragment instanceof HomeFragment) {
                                HomeFragment hf = (HomeFragment) currentFragment;
                                SwipeRefreshLayout srlHome = hf.getView().findViewById(R.id.srlHome);
                                if (srlHome.isRefreshing()) {
                                    srlHome.setRefreshing(false);
                                }
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.detach(currentFragment);
                                fragmentTransaction.attach(currentFragment);
                                fragmentTransaction.commit();
                                bottomNavigation.setSelectedItemId(R.id.navigation_home);
                            }

                        }
                    }
                });
    }

}
