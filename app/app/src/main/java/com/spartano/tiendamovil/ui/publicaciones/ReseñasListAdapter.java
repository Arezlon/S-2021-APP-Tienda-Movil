package com.spartano.tiendamovil.ui.publicaciones;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Comentario;
import com.spartano.tiendamovil.model.Reseña;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReseñasListAdapter extends ArrayAdapter<Reseña> {
    private final Context context;
    private final List<Reseña> reseñas;
    private final LayoutInflater inflater;

    public ReseñasListAdapter(@NonNull Context context, int resource, @NonNull List<Reseña> objects, LayoutInflater layoutInflater) {
        super(context, resource, objects);

        this.context = context;
        this.reseñas = objects;
        this.inflater = layoutInflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = convertView != null ? convertView : inflater.inflate(R.layout.list_item_resena, parent, false);
        Reseña reseña = reseñas.get(position);

        TextView tvReseñaUsuario = convertView.findViewById(R.id.tvReseñaUsuario);
        TextView tvReseñaFecha = convertView.findViewById(R.id.tvReseñaFecha);
        TextView tvReseñaEncabezado = convertView.findViewById(R.id.tvReseñaEncabezado);
        TextView tvReseña = convertView.findViewById(R.id.tvReseña);
        RatingBar rbPuntajeReseña = convertView.findViewById(R.id.rbPuntajeReseña);

        String fecha = reseña.getCreacion().substring(0,10);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyy");

        try {
            Date fechaConvertida = formatter.parse(fecha);
            tvReseñaFecha.setText(formatter2.format(fechaConvertida));
        } catch (ParseException e) {
            tvReseñaFecha.setText("");
            e.printStackTrace();
        }

        tvReseñaUsuario.setText("-" + reseña.getUsuario().getNombre() + " " + reseña.getUsuario().getApellido());
        tvReseña.setText(reseña.getContenido());
        tvReseñaEncabezado.setText(reseña.getEncabezado());
        rbPuntajeReseña.setRating(reseña.getPuntaje());
        //rbPuntajeReseña.setNumStars(reseña.getPuntaje());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }
}
