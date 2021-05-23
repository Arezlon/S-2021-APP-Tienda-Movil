package com.spartano.tiendamovil.ui.nuevaPublicacion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Publicacion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class NuevaPublicacionFragment extends Fragment {
    private EditText etCP_Titulo, etCP_Descripcion, etCP_Precio, etCP_Stock;
    private Spinner spnCP_Categorias, spnCP_Tipos;
    private Button btCP_Confirmar;


    private NuevaPublicacionViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(NuevaPublicacionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_nueva_publicacion, container, false);
        /*final TextView textView = root.findViewById(R.id.nueva_publicacion);
        nuevaPublicacionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        viewModel.getCategoriasMutable().observe(getViewLifecycleOwner(), new Observer<Map<Integer, String>>() {
            @Override
            public void onChanged(Map<Integer, String> listaCategorias) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getContext(),
                        android.R.layout.simple_spinner_item,
                        new ArrayList<String>(listaCategorias.values()));
                spnCP_Categorias.setAdapter(adapter);
            }
        });
        viewModel.getTiposMutable().observe(getViewLifecycleOwner(), new Observer<Map<Integer, String>>() {
            @Override
            public void onChanged(Map<Integer, String> listaTipos) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getContext(),
                        android.R.layout.simple_spinner_item,
                        new ArrayList<String>(listaTipos.values()));
                spnCP_Tipos.setAdapter(adapter);
            }
        });
        viewModel.getErrorMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
            }
        });

        inicializarVista(root);
        viewModel.obtenerListadosDesplegables();
        return root;
    }

    private void inicializarVista(View root){
        etCP_Titulo = root.findViewById(R.id.etCP_Titulo);
        etCP_Descripcion = root.findViewById(R.id.etCP_Descripcion);
        etCP_Precio = root.findViewById(R.id.etCP_Precio);
        etCP_Stock = root.findViewById(R.id.etCP_Stock);
        spnCP_Categorias = root.findViewById(R.id.spnCP_Categorias);
        spnCP_Tipos = root.findViewById(R.id.spnCP_Tipos);
        btCP_Confirmar = root.findViewById(R.id.btCP_Confirmar);

        btCP_Confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Publicacion p = new Publicacion();
                p.setTitulo(etCP_Titulo.getText().toString());
                p.setDescripcion(etCP_Descripcion.getText().toString());
                try { p.setPrecio(Float.parseFloat(etCP_Precio.getText().toString())); } catch (NumberFormatException e) { p.setPrecio(-1); }
                try { p.setStock(Integer.parseInt(etCP_Stock.getText().toString())); } catch (NumberFormatException e) { p.setStock(-1); }
                p.setCategoria(spnCP_Categorias.getSelectedItemPosition()+1);
                p.setTipo(spnCP_Tipos.getSelectedItemPosition()+1);

                viewModel.nuevaPublicacion(p);
            }
        });
    }
}