package com.joelnemi.foro.listeners;


import com.joelnemi.foro.models.Usuario;

import java.io.Serializable;

public interface IPerfilClickListener extends Serializable {

    void onPerfilSelected(Usuario user);

}
