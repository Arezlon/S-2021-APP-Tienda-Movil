package com.spartano.tiendamovil.ui.publicaciones;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
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

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Publicacion;

import java.util.List;

public class PublicacionesListAdapter  extends ArrayAdapter<Publicacion> {
    private final Context context;
    private final List<Publicacion> publicaciones;
    private final LayoutInflater inflater;

    public PublicacionesListAdapter(@NonNull Context context, int resource, @NonNull List<Publicacion> objects, LayoutInflater layoutInflater) {
        super(context, resource, objects);

        this.context = context;
        this.publicaciones = objects;
        this.inflater = layoutInflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = convertView != null ? convertView : inflater.inflate(R.layout.list_item_publicacion, parent, false);
        Publicacion publicacion = publicaciones.get(position);

        Log.d("salida", "getView " + position);

        TextView tvTitulo = convertView.findViewById(R.id.tvTituloPublicacion);
        TextView tvPrecio = convertView.findViewById(R.id.tvPrecioPublicacion);
        ImageView ivFotoPrincipalPublicacion = convertView.findViewById(R.id.ivFotoPrincipalPublicacion);

        tvTitulo.setText(publicacion.getTitulo());
        tvPrecio.setText("$"+publicacion.getPrecio());
        /*try {
            ivFotoPrincipalPublicacion.setImageBitmap();
        } catch (Exception e){
            e.printStackTrace();
        }*/
        //Glide.with(getContext()).load(publicacion.getImagen()).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivFoto);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putSerializable("publicacion", publicacion);
                Navigation.findNavController((Activity)context, R.id.nav_host_fragment).navigate(R.id.nav_publicacion, b); // cambiar por vista detalles
            }
        });

        return convertView;
    }
}
