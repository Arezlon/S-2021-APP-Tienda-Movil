package com.spartano.tiendamovil.ui.notificaciones;

import android.annotation.SuppressLint;
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
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

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
    private final NotificacionesViewModel viewModel;

    public NotificacionesListAdapter(NotificacionesViewModel viewModel, @NonNull Context context, int resource, @NonNull List<Notificacion> objects, LayoutInflater layoutInflater) {
        super(context, resource, objects);

        this.viewModel = viewModel;
        this.context = context;
        this.notificaciones = objects;
        this.inflater = layoutInflater;
    }

    @SuppressLint("ResourceAsColor")
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

        if(notificacion.getEstado() != 1) {
            ivNotificacionNueva.setVisibility(View.INVISIBLE);
            ivNotificacionIcono.setColorFilter(ContextCompat.getColor(getContext(), R.color.notificacionLeida));
            tvNotificacionMensaje.setTextColor(ContextCompat.getColor(context, R.color.notificacionLeida));
        } else {
            ivNotificacionNueva.setVisibility(View.VISIBLE);
            ivNotificacionIcono.setColorFilter(ContextCompat.getColor(getContext(), R.color.notificacionNoLeida));
            tvNotificacionMensaje.setTextColor(ContextCompat.getColor(context, R.color.notificacionNoLeida));
        }


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

            switch (notificacion.getTipo()){
                case 1:
                    ivNotificacionIcono.setImageResource(R.drawable.baseline_sell_24);
                    break;
                case 2:
                    ivNotificacionIcono.setImageResource(R.drawable.baseline_live_help_24);
                    break;
                case 3:
                    ivNotificacionIcono.setImageResource(R.drawable.baseline_star_half_24);
                    break;
                case 4:
                    ivNotificacionIcono.setImageResource(R.drawable.baseline_error_outline_24);
                    break;
                case 5:
                    ivNotificacionIcono.setImageResource(R.drawable.baseline_forum_24);
                    break;
                default:
                    ivNotificacionIcono.setImageResource(R.drawable.baseline_notifications_active_24);
                    break;
            }

            ivNotificacionIr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.LeerNotificacion(notificacion);
                    if(notificacion.getTipo() == 1){
                        Bundle b = new Bundle();
                        b.putSerializable("compra", notificacion.getCompra());
                        Navigation.findNavController((Activity) getContext(), R.id.nav_host_fragment).navigate(R.id.nav_compra, b);
                    }else{  //redirigir a los diferentes tabs según el tipo de notificación
                        Bundle c = new Bundle();
                        c.putSerializable("publicacion", notificacion.getPublicacion());
                        Navigation.findNavController((Activity) getContext(), R.id.nav_host_fragment).navigate(R.id.nav_publicacion, c);
                    }

                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.LeerNotificacion(notificacion);
                }
            });

        }catch (NullPointerException e){
            Log.d("salida", "Error en historial de transacciones, elemento: "+position);
        }
        return convertView;
    }
}
