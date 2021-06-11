package com.spartano.tiendamovil.ui.publicaciones;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.PublicacionEtiqueta;

import java.util.List;

public class EtiquetasRecyclerAdapter extends RecyclerView.Adapter<EtiquetasRecyclerAdapter.ViewHolderEtiqueta> {

    private List<PublicacionEtiqueta> etiquetas;
    private TabPublicacionViewModel viewModel;
    private Context context;
    private boolean publicacionEsMia;

    public EtiquetasRecyclerAdapter(List<PublicacionEtiqueta> etiquetas, Context context, TabPublicacionViewModel viewModel, boolean publicacionEsMia) {
        this.etiquetas = etiquetas;
        this.context = context;
        this.viewModel = viewModel;
        this.publicacionEsMia = publicacionEsMia;
    }

    @NonNull
    @Override
    public EtiquetasRecyclerAdapter.ViewHolderEtiqueta onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_etiqueta, null, false);
        return new EtiquetasRecyclerAdapter.ViewHolderEtiqueta(view, context, viewModel, publicacionEsMia);
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

        TabPublicacionViewModel viewModel;
        boolean permitirEliminacion;

        public ViewHolderEtiqueta(@NonNull View itemView, Context context, TabPublicacionViewModel viewModel, boolean permitirEliminacion) {
            super(itemView);
            this.itemView = itemView;
            this.context = context;
            this.viewModel = viewModel;
            this.permitirEliminacion = permitirEliminacion;
            tvEtiquetaNombre = itemView.findViewById(R.id.tvEtiquetaNombre);
        }

        public void mostrarDatos(PublicacionEtiqueta etiqueta){
            // Settear los datos a cada elemento
            tvEtiquetaNombre.setText(etiqueta.getEtiqueta().getNombre());

            if (permitirEliminacion) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(context)
                                .setTitle("Etiqueta")
                                .setMessage("¿Quiere eliminar la etiqueta \"" + etiqueta.getEtiqueta().getNombre() + "\" de la publicación?")
                                .setPositiveButton("Si, eliminar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        viewModel.deleteEtiqueta(etiqueta);
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                });
            }
        }
    }
}