package com.spartano.tiendamovil.ui.perfil;

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
import com.spartano.tiendamovil.model.Compra;
import com.spartano.tiendamovil.request.ApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TabComprasListAdapter extends ArrayAdapter<Compra> {
    private final Context context;
    private final List<Compra> compras;
    private final LayoutInflater inflater;

    public TabComprasListAdapter(@NonNull Context context, int resource, @NonNull List<Compra> objects, LayoutInflater layoutInflater) {
        super(context, resource, objects);

        this.context = context;
        this.compras = objects;
        this.inflater = layoutInflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = convertView != null ? convertView : inflater.inflate(R.layout.list_item_compra, parent, false);
        Compra compra = compras.get(position);

        TextView tvLCompraTituloPublicacion = convertView.findViewById(R.id.tvLCompraTituloPublicacion);
        TextView tvLCompraCantidad = convertView.findViewById(R.id.tvLCompraCantidad);
        TextView tvLCompraImporte = convertView.findViewById(R.id.tvLCompraImporte);
        TextView tvLCompraFecha = convertView.findViewById(R.id.tvLCompraFecha);
        ImageView ivLCompraImagen = convertView.findViewById(R.id.ivLCompraImagen);

        try {
            tvLCompraTituloPublicacion.setText(compra.getPublicacion().getTitulo());
            tvLCompraCantidad.setText(compra.getCantidad() + " unidad/es");
            tvLCompraImporte.setText("$" + (int) compra.getPrecio());

            String fecha = compra.getCreacion().substring(0, 10);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyy");

            Glide.with(getContext())
                    .load(ApiClient.getPath()+compra.getPublicacion().getImagenDir())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivLCompraImagen);

            try {
                Date fechaConvertida = formatter.parse(fecha);
                tvLCompraFecha.setText(formatter2.format(fechaConvertida));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putSerializable("compra", compra);
                    Navigation.findNavController((Activity) getContext(), R.id.nav_host_fragment).navigate(R.id.nav_compra, b);
                }
            });

        }catch (NullPointerException e){
            Log.d("salida", "Error en historial de transacciones, elemento: "+position);
        }
        return convertView;
    }
}
