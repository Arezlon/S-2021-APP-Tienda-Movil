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

import com.spartano.tiendamovil.MenuNavegacionActivity;
import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Compra;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CompraFragment extends Fragment {

    private CompraViewModel viewModel;
    private TextView tvCompraTituloPublicacion, tvCompraCantidad, tvCompraComprador, tvCompraId, tvCompraImporte, tvCompraVendedor, tvCompraFecha;
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
        tvCompraVendedor = root.findViewById(R.id.tvCompraVendedor);
        tvCompraId = root.findViewById(R.id.tvCompraId);
        tvCompraImporte = root.findViewById(R.id.tvCompraImporte);
        tvCompraComprador = root.findViewById(R.id.tvCompraComprador);
        tvCompraFecha = root.findViewById(R.id.tvCompraFecha);

        tvCompraTituloPublicacion.setText(compraActual.getPublicacion().getTitulo());
        tvCompraCantidad.setText(compraActual.getCantidad()+" unidad/es");
        tvCompraComprador.setText("Comprador: "+compraActual.getUsuario().getApellido()+" "+compraActual.getUsuario().getNombre());
        tvCompraId.setText("Compra #"+compraActual.getId());
        tvCompraImporte.setText("$"+compraActual.getPrecio());
        tvCompraVendedor.setText("Vendedor: "+compraActual.getPublicacion().getUsuario().getApellido()+" "+compraActual.getPublicacion().getUsuario().getNombre());

        String fecha = compraActual.getCreacion().substring(0,10);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyy");
        try {
            Date fechaConvertida = formatter.parse(fecha);
            tvCompraFecha.setText(formatter2.format(fechaConvertida));
        } catch (ParseException e) {
            e.printStackTrace();
            tvCompraFecha.setText("Error de fecha");
        }
    }

}