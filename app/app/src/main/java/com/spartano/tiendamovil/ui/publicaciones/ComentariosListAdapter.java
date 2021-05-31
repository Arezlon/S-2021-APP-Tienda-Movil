package com.spartano.tiendamovil.ui.publicaciones;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ComentariosListAdapter extends ArrayAdapter<Comentario> {
    private final Context context;
    private final List<Comentario> comentarios;
    private final LayoutInflater inflater;
    private boolean publicacionEsMia;

    public ComentariosListAdapter(@NonNull Context context, int resource, @NonNull List<Comentario> objects, LayoutInflater layoutInflater, boolean publicacionEsMia) {
        super(context, resource, objects);

        this.context = context;
        this.comentarios = objects;
        this.inflater = layoutInflater;
        this.publicacionEsMia = publicacionEsMia;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = convertView != null ? convertView : inflater.inflate(R.layout.list_item_comentario, parent, false);
        Comentario comentario = comentarios.get(position);

        TextView tvComentarioUsuario = convertView.findViewById(R.id.tvComentarioUsuario);
        TextView tvComentarioPregunta = convertView.findViewById(R.id.tvComentarioPregunta);
        TextView tvComentarioRespuesta = convertView.findViewById(R.id.tvComentarioRespuesta);
        TextView tvComentarioFecha = convertView.findViewById(R.id.tvComentarioFecha);
        TextView icRespuesta = convertView.findViewById(R.id.icRespuesta);
        Button btResponderComentario = convertView.findViewById(R.id.btResponderComentario);

        String fecha = comentario.getCreacion().substring(0,10);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyy");

        try {
            Date fechaConvertida = formatter.parse(fecha);
            tvComentarioFecha.setText(formatter2.format(fechaConvertida));
        } catch (ParseException e) {
            tvComentarioFecha.setText("");
            e.printStackTrace();
        }

        tvComentarioUsuario.setText(comentario.getUsuario().getNombre() + " " + comentario.getUsuario().getApellido());
        tvComentarioPregunta.setText(comentario.getPregunta());
        if (comentario.respuesta != null) {
            // Si hay respuesta, mostrar el texto, mostrar el icono de subdirectorio, ocultar el boton "responder"
            tvComentarioRespuesta.setVisibility(View.VISIBLE);
            btResponderComentario.setVisibility(View.GONE);

            tvComentarioRespuesta.setText(comentario.getRespuesta());
            icRespuesta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_subdirectory_arrow_right_24, 0, 0, 0);
        } else if (publicacionEsMia) {
            // Si no hay respuesta y la publicacion es mía, mostrar el icono de "pendiente" y el boton "responder"
            tvComentarioRespuesta.setVisibility(View.GONE);
            btResponderComentario.setVisibility(View.VISIBLE);

            icRespuesta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_pending_24, 0, 0, 0);
        } else {
            // Si no hay respuesta y la publicacion no es mia, mostrar el icono "pendiente" y el texto de respuesta pendiente
            tvComentarioRespuesta.setVisibility(View.VISIBLE);
            btResponderComentario.setVisibility(View.GONE);

            tvComentarioRespuesta.setText("El vendedor todavía no respondió");
            icRespuesta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_pending_24, 0, 0, 0);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }
}
