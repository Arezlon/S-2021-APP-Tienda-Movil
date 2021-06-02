package com.spartano.tiendamovil.ui.publicaciones;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.spartano.tiendamovil.model.Etiqueta;
import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.request.ApiClient;
import com.spartano.tiendamovil.ui.inicio.PublicacionesRecyclerAdapter;

import java.util.List;

public class EtiquetasRecyclerAdapter extends RecyclerView.Adapter<EtiquetasRecyclerAdapter.ViewHolderEtiqueta> {

    private List<Etiqueta> etiquetas;
    private Context context;

    public EtiquetasRecyclerAdapter(List<Etiqueta> etiquetas, Context context) {
        this.etiquetas = etiquetas;
        this.context = context;
    }

    @NonNull
    @Override
    public EtiquetasRecyclerAdapter.ViewHolderEtiqueta onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_etiqueta, null, false);
        return new EtiquetasRecyclerAdapter.ViewHolderEtiqueta(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull EtiquetasRecyclerAdapter.ViewHolderEtiqueta holder, int position) {
        holder.mostrarDatos(etiquetas.get(position));
    }

    @Override
    public int getItemCount() {
        return etiquetas.size();
    }

    public static class ViewHolderEtiqueta extends RecyclerView.ViewHolder {

        // Referencias a los elementos de los items
        View itemView;
        TextView tvEtiquetaNombre;
        Context context;

        public ViewHolderEtiqueta(@NonNull View itemView, Context context) {
            super(itemView);
            this.itemView = itemView;
            this.context = context;
            tvEtiquetaNombre = itemView.findViewById(R.id.tvEtiquetaNombre);
        }

        public void mostrarDatos(Etiqueta etiqueta){
            // Settear los datos a cada elemento
            tvEtiquetaNombre.setText(etiqueta.getNombre());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("salida", "desde el recycler adapter: " + etiqueta.getNombre());
                    //Navigation.findNavController((Activity)context, R.id.nav_host_fragment).navigate(R.id.nav_inicio);
                }
            });
        }
    }
}