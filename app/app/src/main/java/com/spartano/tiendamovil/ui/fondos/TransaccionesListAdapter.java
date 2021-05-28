package com.spartano.tiendamovil.ui.fondos;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Transaccion;

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

        tvHistorialImporte.setText("$"+transaccion.getImporte());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //redireccion a detalles de la compra (en casos de compra/venta)
            }
        });

        return convertView;
    }
}
