package com.joelnemi.foro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.joelnemi.foro.R;

import java.util.ArrayList;

public class AdaptadorOpciones extends ArrayAdapter<String> {
    private ArrayList<String> opciones;
    private Context context;

    public AdaptadorOpciones(@NonNull Context context,int resource, ArrayList<String> opciones) {
        super(context,resource,opciones);
        this.opciones=opciones;
        this.context=context;
    }

    @NonNull
    public View getCustomView(int position, @Nullable View convertView, @NonNull ViewGroup parent)  {

        View item = convertView;
        ViewHolder holder;

        if (item==null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.custom_spinner_dropdown_item, null);

            holder=new ViewHolder();
            holder.iconoOpcion = item.findViewById(R.id.ivImageOpcion);
            holder.nombreOpcion = item.findViewById(R.id.ctvNombreOpcion);


            item.setTag(holder);
        }else {
            holder = (ViewHolder) item.getTag();
        }

        holder.nombreOpcion.setText(opciones.get(position));

        switch (opciones.get(position)){

        }
        holder.iconoOpcion.setImageResource(R.drawable.casa);

        return item;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public void setDropDownViewResource(int resource) {
        super.setDropDownViewResource(resource);
    }

    static class ViewHolder{
        TextView nombreOpcion;
        ImageView iconoOpcion;
    }
}
