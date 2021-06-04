package com.spartano.tiendamovil.ui.notificaciones;

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
import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Notificacion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificacionesListAdapter extends ArrayAdapter<Notificacion> {
    private final Context context;
    private final List<Notificacion> notificaciones;
    private final LayoutInflater inflater;

    public NotificacionesListAdapter(@NonNull Context context, int resource, @NonNull List<Notificacion> objects, LayoutInflater layoutInflater) {
        super(context, resource, objects);

        this.context = context;
        this.notificaciones = objects;
        this.inflater = layoutInflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = convertView != null ? convertView : inflater.inflate(R.layout.list_item_notificacion, parent, false);
        Notificacion notificacion = notificaciones.get(position);

        TextView tvNotificacionMensaje = convertView.findViewById(R.id.tvNotificacionMensaje);
        TextView tvNotificacionFecha = convertView.findViewById(R.id.tvNotificacionFecha);

        ImageView ivNotificacionIr = convertView.findViewById(R.id.ivNotificacionIr);
        ImageView ivNotificacionIcono = convertView.findViewById(R.id.ivNotificacionIcono);
        ImageView ivNotificacionNueva = convertView.findViewById(R.id.ivNotificacionNueva);

        if(notificacion.estado != 1)
            ivNotificacionNueva.setVisibility(View.INVISIBLE);

        try {
            tvNotificacionMensaje.setText(notificacion.getMensaje());

            String fecha = notificacion.getCreacion().substring(0, 10);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyy");

            try {
                Date fechaConvertida = formatter.parse(fecha);
                tvNotificacionFecha.setText(formatter2.format(fechaConvertida));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            switch (notificacion.tipo){
                case 1:
                    //ivNotificacionIcono.setColorFilter(ContextCompat.getColor(getContext(), R.color.hCarga));
                    ivNotificacionIcono.setImageResource(R.drawable.baseline_sell_24);
                case 2:
                    ivNotificacionIcono.setImageResource(R.drawable.baseline_live_help_24);
                case 3:
                    ivNotificacionIcono.setImageResource(R.drawable.baseline_star_half_24);
                case 4:
                    ivNotificacionIcono.setImageResource(R.drawable.baseline_error_outline_24);
                case 5:
                    ivNotificacionIcono.setImageResource(R.drawable.baseline_forum_24);
                default:
                    ivNotificacionIcono.setImageResource(R.drawable.baseline_notifications_active_24);
            }

            ivNotificacionIr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Bundle b = new Bundle();
                    b.putSerializable("compra", compra);
                    Navigation.findNavController((Activity) getContext(), R.id.nav_host_fragment).navigate(R.id.nav_compra, b);*/
                    Log.d("salida", "redireccion a publicación/compra");
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("salida", "cambiar estado de la notificacion a 2 (leida)"); //recargar ?
                }
            });

        }catch (NullPointerException e){
            Log.d("salida", "Error en historial de transacciones, elemento: "+position);
        }
        return convertView;
    }
}
