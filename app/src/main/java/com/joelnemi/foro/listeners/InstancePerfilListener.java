package com.joelnemi.foro.listeners;

import android.content.Context;
import android.content.Intent;
import com.joelnemi.foro.activities.ProfileActivity;
import com.joelnemi.foro.fragments.HomeFragment;
import com.joelnemi.foro.models.Post;
import com.joelnemi.foro.models.Usuario;

import java.util.ArrayList;

public class InstancePerfilListener implements IPerfilClickListener{

    private static InstancePerfilListener listener;

    private InstancePerfilListener() {
    }

    public static InstancePerfilListener getInstance() {
        if (listener == null){
            listener = new InstancePerfilListener();
            return listener;
        }
        return listener;
    }

    @Override
    public void onPerfilSelected(Usuario user, Context context) {
        Intent iProfile = new Intent(context, ProfileActivity.class);
        iProfile.putExtra("usuario", user);
        iProfile.putExtra("listener", listener);
        context.startActivity(iProfile);
    }
}
