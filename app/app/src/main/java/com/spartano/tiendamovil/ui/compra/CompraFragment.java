package com.spartano.tiendamovil.ui.compra;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Compra;

public class CompraFragment extends Fragment {

    private CompraViewModel viewModel;
    private TextView tvCompraTituloPublicacion, tvCompraCantidad, tvCompraComprador, tvCompraId, tvCompraImporte, tvCompraVendedor, tvCompraFecha;
    private Button btCompraReseña;
    private Compra compraActual;

    public static CompraFragment newInstance() {
        return new CompraFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_compra, container, false);
        viewModel = new ViewModelProvider(this).get(CompraViewModel.class);
        viewModel.getCompraMutable().observe(getViewLifecycleOwner(), new Observer<Compra>() {
            @Override
            public void onChanged(Compra compra) {
                compraActual = compra;
                inicializarVista(root);
                //Actualizar datos del usuario (fondos) en el menú
            }
        });
        viewModel.ObtenerCompra(getArguments());
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void inicializarVista(View root){
        tvCompraTituloPublicacion = root.findViewById(R.id.tvCompraTituloPublicacion);
        tvCompraCantidad = root.findViewById(R.id.tvCompraCantidad);
        tvCompraComprador = root.findViewById(R.id.tvCompraComprador);
        tvCompraId = root.findViewById(R.id.tvCompraId);
        tvCompraImporte = root.findViewById(R.id.tvCompraImporte);
        tvCompraVendedor = root.findViewById(R.id.tvCompraVendedor);
        tvCompraFecha = root.findViewById(R.id.tvCompraFecha);
        btCompraReseña = root.findViewById(R.id.btCompraReseña);

        tvCompraId.setText("#"+compraActual.getId());
    }

}