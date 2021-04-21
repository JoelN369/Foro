package com.joelnemi.foro;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;


public class CrearComunidadDialog {

    private static Activity activity;
    private AlertDialog dialog;

    private Button bAceptar;
    private Button bCancelar;
    private EditText etComunidad;
    private FirebaseFirestore db;

    public CrearComunidadDialog(Activity activity2){
        activity = activity2;
        db = FirebaseFirestore.getInstance();
    }

    public void startDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();

        View v = inflater.inflate(R.layout.crear_comunidad_dialog, null);

        bAceptar = v.findViewById(R.id.bAceptar);
        bCancelar = v.findViewById(R.id.bCancelar);
        etComunidad = v.findViewById(R.id.etNombreComunidad);

        bAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etComunidad.getText().length() > 3) {
                    db.collection("categoria").document(etComunidad.getText().toString())
                            .set(new String(etComunidad.getText().toString()));
                }else{
                    Toast.makeText(activity,"El nombre tiene que tener mas de 3 letras", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissmisDialog();
            }
        });


        builder.setView(v);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }

    public void dissmisDialog(){
        dialog.dismiss();
    }


}
