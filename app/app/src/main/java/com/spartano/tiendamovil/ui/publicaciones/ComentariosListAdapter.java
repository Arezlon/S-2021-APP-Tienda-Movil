package com.spartano.tiendamovil.ui.publicaciones;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Comentario;
import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.request.ApiClient;

import java.util.List;

public class ComentariosListAdapter extends ArrayAdapter<Comentario> {
    private final Context context;
    private final List<Comentario> comentarios;
    private final LayoutInflater inflater;

    public ComentariosListAdapter(@NonNull Context context, int resource, @NonNull List<Comentario> objects, LayoutInflater layoutInflater) {
        super(context, resource, objects);

        this.context = context;
        this.comentarios = objects;
        this.inflater = layoutInflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = convertView != null ? convertView : inflater.inflate(R.layout.list_item_comentario, parent, false);
        Comentario comentario = comentarios.get(position);

        TextView tvComentarioUsuario = convertView.findViewById(R.id.tvComentarioUsuario);
        TextView tvComentarioPregunta = convertView.findViewById(R.id.tvComentarioPregunta);
        TextView tvComentarioRespuesta = convertView.findViewById(R.id.tvComentarioRespuesta);
        TextView icRespuesta = convertView.findViewById(R.id.icRespuesta);
        TextView tvRespuesta = convertView.findViewById(R.id.tvRespuesta);

        tvComentarioUsuario.setText(comentario.getUsuario().getNombre() + " " + comentario.getUsuario().getApellido());
        tvComentarioPregunta.setText(comentario.getPregunta());
        if (comentario.respuesta != null) {
            tvComentarioRespuesta.setText(comentario.getRespuesta());
        } else {
            tvRespuesta.setText("El vendedor todavía no respondió");
            tvComentarioRespuesta.setVisibility(View.INVISIBLE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }
}
