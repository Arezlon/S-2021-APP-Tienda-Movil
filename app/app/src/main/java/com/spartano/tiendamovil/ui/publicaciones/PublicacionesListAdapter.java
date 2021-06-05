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
import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.request.ApiClient;

import java.util.List;

public class PublicacionesListAdapter extends ArrayAdapter<Publicacion> {
    private final Context context;
    private final List<Publicacion> publicaciones;
    private final LayoutInflater inflater;
    private final boolean publicacionesMias;

    public PublicacionesListAdapter(@NonNull Context context, int resource, @NonNull List<Publicacion> objects, LayoutInflater layoutInflater, boolean publicacionesMias) {
        super(context, resource, objects);

        this.context = context;
        this.publicaciones = objects;
        this.inflater = layoutInflater;
        this.publicacionesMias = publicacionesMias;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = convertView != null ? convertView : inflater.inflate(R.layout.list_item_publicacion, parent, false);
        Publicacion publicacion = publicaciones.get(position);

        TextView tvTitulo = convertView.findViewById(R.id.tvPublicacionTitulo);
        TextView tvPrecio = convertView.findViewById(R.id.tvPrecioPublicacion);

        ImageView ivFotoPrincipalPublicacion = convertView.findViewById(R.id.ivFotoPrincipalPublicacion);

        tvTitulo.setText(publicacion.getTitulo());
        tvPrecio.setText("$"+publicacion.getPrecio());

        TextView tvStock = convertView.findViewById(R.id.tvStock);
        TextView tvVentas = convertView.findViewById(R.id.tvVentas);
        if (publicacionesMias) {
            tvStock.setText("Stock: " + publicacion.getStock());
            tvVentas.setText("Ventas: 0");
        } else {
            tvStock.setText("");
            tvVentas.setText("");
        }

        Glide.with(getContext())
                .load(ApiClient.getPath()+publicacion.getImagenDir())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(ivFotoPrincipalPublicacion);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putSerializable("publicacion", publicacion);
                Navigation.findNavController((Activity)context, R.id.nav_host_fragment).navigate(R.id.nav_publicacion, b);
            }
        });

        return convertView;
    }
}
