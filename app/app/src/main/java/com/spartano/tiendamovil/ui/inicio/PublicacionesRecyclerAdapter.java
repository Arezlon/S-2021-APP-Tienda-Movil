package com.spartano.tiendamovil.ui.inicio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Publicacion;

import java.util.List;

public class PublicacionesRecyclerAdapter extends RecyclerView.Adapter<PublicacionesRecyclerAdapter.ViewHolderPublicacion> {

    private List<Publicacion> publicaciones;

    public PublicacionesRecyclerAdapter(List<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
    }

    @NonNull
    @Override
    public ViewHolderPublicacion onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_publicacion, null, false);
        return new ViewHolderPublicacion(view);
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

        public ViewHolderPublicacion(@NonNull View itemView) {
            super(itemView);
            // Asignaciones de los elementos de los items (itemView.findViewById())
        }

        public void mostrarDatos(Publicacion publicacion){
            // Settear los datos a cada elemento
        }
    }
}
