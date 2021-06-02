package com.spartano.tiendamovil.ui.perfil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Compra;

import java.util.List;

public class TabVentasListAdapter extends ArrayAdapter<Compra> {
    private final Context context;
    private final List<Compra> compras;
    private final LayoutInflater inflater;

    public TabVentasListAdapter(@NonNull Context context, int resource, @NonNull List<Compra> objects, LayoutInflater layoutInflater) {
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

        //

        return convertView;
    }
}
