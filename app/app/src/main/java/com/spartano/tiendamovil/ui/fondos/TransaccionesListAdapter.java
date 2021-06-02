package com.spartano.tiendamovil.ui.fondos;

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
import com.spartano.tiendamovil.model.Transaccion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TransaccionesListAdapter extends ArrayAdapter<Transaccion> {
    private final Context context;
    private final List<Transaccion> transacciones;
    private final LayoutInflater inflater;

    public TransaccionesListAdapter(@NonNull Context context, int resource, @NonNull List<Transaccion> objects, LayoutInflater layoutInflater) {
        super(context, resource, objects);

        this.context = context;
        this.transacciones = objects;
        this.inflater = layoutInflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = convertView != null ? convertView : inflater.inflate(R.layout.list_item_transaccion, parent, false);
        Transaccion transaccion = transacciones.get(position);

        TextView tvHistorialImporte = convertView.findViewById(R.id.tvHistorialImporte);
        TextView tvHistorialBalance = convertView.findViewById(R.id.tvHistorialBalance);
        TextView tvHistorialFechaHora = convertView.findViewById(R.id.tvHistorialFechaHora);
        TextView tvHistorialCompra = convertView.findViewById(R.id.tvHistorialCompra);
        ImageView ivHistorialIcono = convertView.findViewById(R.id.ivHistorialIcono);

        String fecha = transaccion.getCreacion().substring(0,10);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyy");
        try {
            Date fechaConvertida = formatter.parse(fecha);
            tvHistorialFechaHora.setText(formatter2.format(fechaConvertida));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvHistorialBalance.setText("$"+(int)transaccion.getBalance());
        try{
            if(transaccion.tipo == 1){
                tvHistorialImporte.setText("+$"+(int)transaccion.getImporte());
                ivHistorialIcono.setImageResource(R.drawable.baseline_payments_24);
                ivHistorialIcono.setColorFilter(ContextCompat.getColor(getContext(), R.color.hCarga));
                tvHistorialCompra.setText("");
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
                    }
                });

            }else if(transaccion.tipo == 2){
                tvHistorialImporte.setText("-$"+(int)transaccion.getImporte());
                ivHistorialIcono.setImageResource(R.drawable.baseline_shopping_bag_24);
                ivHistorialIcono.setColorFilter(ContextCompat.getColor(getContext(), R.color.hCompra));
                tvHistorialCompra.setText(transaccion.getCompra().getPublicacion().getTitulo());

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle b = new Bundle();
                        b.putSerializable("compra", transaccion.getCompra());
                        Navigation.findNavController((Activity)getContext(), R.id.nav_host_fragment).navigate(R.id.nav_compra, b);
                    }
                });

            }else if(transaccion.tipo == 3){
                tvHistorialImporte.setText("+$"+(int)transaccion.getImporte());
                ivHistorialIcono.setImageResource(R.drawable.baseline_sell_24);
                ivHistorialIcono.setColorFilter(ContextCompat.getColor(getContext(), R.color.hVenta));
                tvHistorialCompra.setText(transaccion.getCompra().getPublicacion().getTitulo());

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {
                        Bundle b = new Bundle();
                        b.putSerializable("compra", transaccion.getCompra());
                        Navigation.findNavController((Activity)getContext(), R.id.nav_host_fragment).navigate(R.id.nav_compra, b);
                    }
                });

            }
        }catch (NullPointerException e){
            Log.d("salida", "Error en historial de transacciones, elemento: "+position);
        }

        return convertView;
    }
}
