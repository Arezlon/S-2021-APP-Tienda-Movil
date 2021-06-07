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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Compra;

import java.util.ArrayList;
import java.util.List;

public class TabVentasFragment extends Fragment {

    public MiPerfilViewModel viewModel;
    private ListView lvVentas;
    private TextView tvListaVentasVacia;
    private ImageView ivListaVentasVacia;

    public static TabVentasFragment newInstance() {
        return new TabVentasFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MiPerfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tab_perfil_ventas, container, false);

        lvVentas = root.findViewById(R.id.lvVentas);

        viewModel.getListaVentasVaciaMutable().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                tvListaVentasVacia = root.findViewById(R.id.tvListaVentasVacia);
                ivListaVentasVacia = root.findViewById(R.id.ivListaVentasVacia);
                tvListaVentasVacia.setVisibility(bool ? View.VISIBLE : View.INVISIBLE);
                ivListaVentasVacia.setVisibility(bool ? View.VISIBLE : View.INVISIBLE);
                tvListaVentasVacia.setText("No se encontraron ventas");
                ivListaVentasVacia.setImageResource(R.drawable.baseline_error_outline_24);
            }
        });

        viewModel.getErrorMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

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