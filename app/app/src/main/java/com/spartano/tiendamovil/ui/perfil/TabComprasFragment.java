package com.spartano.tiendamovil.ui.perfil;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Compra;

import java.util.ArrayList;
import java.util.List;

public class TabComprasFragment extends Fragment {
    public PerfilViewModel viewModel;
    private ListView lvCompras;

    public static TabComprasFragment newInstance() {
        return new TabComprasFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tab_perfil_compras, container, false);

        lvCompras = root.findViewById(R.id.lvCompras);
        viewModel.getComprasMutable().observe(getViewLifecycleOwner(), new Observer<List<Compra>>() {
            @Override
            public void onChanged(List<Compra> compras) {
                ArrayList<Compra> arrayList = new ArrayList<Compra>(compras);
                ArrayAdapter<Compra> adapter = new TabComprasListAdapter(getContext(),
                        R.layout.list_item_compra, arrayList,
                        getLayoutInflater());
                lvCompras.setAdapter(adapter);
            }
        });

        viewModel.ObtenerCompras();
        return root;
    }

}