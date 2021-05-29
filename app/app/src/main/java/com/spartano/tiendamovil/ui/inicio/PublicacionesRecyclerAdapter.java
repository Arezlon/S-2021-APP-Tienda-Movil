package com.spartano.tiendamovil.ui.inicio;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.request.ApiClient;

import org.w3c.dom.Text;

import java.util.List;

public class PublicacionesRecyclerAdapter extends RecyclerView.Adapter<PublicacionesRecyclerAdapter.ViewHolderPublicacion> {

    private List<Publicacion> publicaciones;
    private Context context;

    public PublicacionesRecyclerAdapter(List<Publicacion> publicaciones, Context context) {
        this.publicaciones = publicaciones;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderPublicacion onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_publicacion, null, false);
        return new ViewHolderPublicacion(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPublicacion holder, int position) {
        holder.mostrarDatos(publicaciones.get(position));
    }

    @Override
    public int getItemCount() {
        return publicaciones.size();
    }

    public static class ViewHolderPublicacion extends RecyclerView.ViewHolder {

        // Referencias a los elementos de los items
        View itemView;
        TextView tvPrecioDestacada;
        ImageView ivImagenDestacada;
        Context context;

        public ViewHolderPublicacion(@NonNull View itemView, Context context) {
            super(itemView);
            this.itemView = itemView;
            this.context = context;
            tvPrecioDestacada = itemView.findViewById(R.id.tvPrecioDestacada);
            ivImagenDestacada = itemView.findViewById(R.id.ivImagenDestacada);
            // Asignaciones de los elementos de los items (itemView.findViewById())
        }

        public void mostrarDatos(Publicacion publicacion){
            // Settear los datos a cada elemento
            tvPrecioDestacada.setText("$"+publicacion.getPrecio());
            Glide.with(context)
                    .load(ApiClient.getPath()+publicacion.getImagenDir())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivImagenDestacada);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putSerializable("publicacion", publicacion);
                    Navigation.findNavController((Activity)context, R.id.nav_host_fragment).navigate(R.id.nav_publicacion, b);
                }
            });
        }
    }
}
