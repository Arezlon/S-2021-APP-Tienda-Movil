package com.spartano.tiendamovil.ui.busqueda;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.ui.publicaciones.PublicacionesListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BusquedaFragment extends Fragment {

    private BusquedaViewModel viewModel;
    private ListView lvPublicacionesBusqueda;
    private EditText etBusqueda, etBuscarPrecioMaximo;
    private Button btBuscar;
    private Spinner spnBuscarCategoria, spnBuscarEstado;

    private ImageView ivListaPublicacionesVacia;
    private TextView tvListaPublicacionesVacia;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(BusquedaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_busqueda, container, false);

        viewModel.getBusquedaInicialMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                etBusqueda.setText(s);
            }
        });

        viewModel.getCategoriasMutable().observe(getViewLifecycleOwner(), new Observer<Map<Integer, String>>() {
            @Override
            public void onChanged(Map<Integer, String> listaCategorias) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getContext(),
                        android.R.layout.simple_spinner_item,
                        new ArrayList<String>(listaCategorias.values()));
                spnBuscarCategoria.setAdapter(adapter);
            }
        });

        viewModel.getTiposMutable().observe(getViewLifecycleOwner(), new Observer<Map<Integer, String>>() {
            @Override
            public void onChanged(Map<Integer, String> listaTipos) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getContext(),
                        android.R.layout.simple_spinner_item,
                        new ArrayList<String>(listaTipos.values()));
                spnBuscarEstado.setAdapter(adapter);
            }
        });

        viewModel.getPublicacionesMutable().observe(getViewLifecycleOwner(), new Observer<List<Publicacion>>() {
            @Override
            public void onChanged(List<Publicacion> publicaciones) {
                //ArrayList<Publicacion> arrayList = new ArrayList<Publicacion>(publicaciones);
                ArrayAdapter<Publicacion> adapter = new PublicacionesListAdapter(getContext(),
                        R.layout.list_item_publicacion, publicaciones,
                        getLayoutInflater(), false);
                lvPublicacionesBusqueda.setAdapter(adapter);
            }
        });

        viewModel.getListaPublicacionesVaciaMutable().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {

                tvListaPublicacionesVacia.setVisibility(bool ? View.VISIBLE : View.INVISIBLE);
                ivListaPublicacionesVacia.setVisibility(bool ? View.VISIBLE : View.INVISIBLE);
                tvListaPublicacionesVacia.setText("No se encontraron publicaciones");
                ivListaPublicacionesVacia.setImageResource(R.drawable.baseline_error_outline_24);
            }
        });

        viewModel.getErrorMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        inicializarVista(root);
        viewModel.busquedaInicial(getArguments());
        viewModel.obtenerListadosDesplegables();
        return root;
    }

    private void inicializarVista(View root) {
        tvListaPublicacionesVacia = root.findViewById(R.id.tvListaPublicacionesVacia);
        ivListaPublicacionesVacia = root.findViewById(R.id.ivListaPublicacionesVacia);

        lvPublicacionesBusqueda = root.findViewById(R.id.lvPublicacionesBusqueda);
        etBusqueda = root.findViewById(R.id.etBusqueda);
        btBuscar = root.findViewById(R.id.btBuscar);
        etBuscarPrecioMaximo = root.findViewById(R.id.etBuscarPrecioMaximo);
        spnBuscarCategoria = root.findViewById(R.id.spnBuscarCategoria);
        spnBuscarEstado = root.findViewById(R.id.spnBuscarEstado);

        btBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String busqueda = etBusqueda.getText().toString();
                float precioMaximo;
                try { precioMaximo = Float.parseFloat(etBuscarPrecioMaximo.getText().toString()); } catch (NumberFormatException e) { precioMaximo = -1; }
                // Aca no le sumamos 1 a los valores del enum porque ya hay un objeto agregado con la categoría y tipo "cualquiera"
                int categoria = spnBuscarCategoria.getSelectedItemPosition();
                int tipo = spnBuscarEstado.getSelectedItemPosition();
                viewModel.buscar(busqueda, precioMaximo, categoria, tipo);

                tvListaPublicacionesVacia.setText("Buscando...");
                ivListaPublicacionesVacia.setImageResource(R.drawable.baseline_refresh_24);
            }
        });
    }
}