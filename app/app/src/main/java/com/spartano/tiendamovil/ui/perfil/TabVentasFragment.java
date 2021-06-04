package com.spartano.tiendamovil.ui.perfil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Compra;

import java.util.ArrayList;
import java.util.List;

public class TabVentasFragment extends Fragment {

    public PerfilViewModel viewModel;
    private ListView lvVentas;

    public static TabVentasFragment newInstance() {
        return new TabVentasFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tab_perfil_ventas, container, false);

        lvVentas = root.findViewById(R.id.lvVentas);
        viewModel.getVentasMutable().observe(getViewLifecycleOwner(), new Observer<List<Compra>>() {
            @Override
            public void onChanged(List<Compra> ventas) {
                ArrayList<Compra> arrayList = new ArrayList<Compra>(ventas);
                ArrayAdapter<Compra> adapter = new TabVentasListAdapter(getContext(),
                        R.layout.list_item_compra, arrayList,
                        getLayoutInflater());
                lvVentas.setAdapter(adapter);
            }
        });

        viewModel.ObtenerVentas();
        return root;
    }

}