package com.spartano.tiendamovil.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.model.Usuario;
import com.spartano.tiendamovil.ui.publicaciones.PublicacionesListAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabPublicacionesFragment extends Fragment {
    public PerfilViewModel viewModel;
    private ListView lvPublicaciones;
    private TextView tvListaPublicacionesVacia;
    private ImageView ivListaPublicacionesVacia;

    private List<Publicacion> publicaciones;
    private Usuario usuarioActual;

    public TabPublicacionesFragment(Usuario usuario) {
        usuarioActual = usuario;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_publicaciones, container, false);

        lvPublicaciones = root.findViewById(R.id.lvPublicaciones);

        viewModel.getPublicacionesMutable().observe(getViewLifecycleOwner(), new Observer<List<Publicacion>>() {
            @Override
            public void onChanged(List<Publicacion> lista) {
                publicaciones = lista;
                ArrayList<Publicacion> arrayList = new ArrayList<Publicacion>(publicaciones);
                ArrayAdapter<Publicacion> adapter = new PublicacionesListAdapter(getContext(),
                        R.layout.list_item_publicacion, arrayList,
                        getLayoutInflater(), false);
                lvPublicaciones.setAdapter(adapter);
            }
        });

        viewModel.getErrorMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getListaPublicacionesVaciaMutable().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                tvListaPublicacionesVacia = root.findViewById(R.id.tvListaPublicacionesVacia);
                ivListaPublicacionesVacia = root.findViewById(R.id.ivListaPublicacionesVacia);
                tvListaPublicacionesVacia.setVisibility(bool ? View.VISIBLE : View.INVISIBLE);
                ivListaPublicacionesVacia.setVisibility(bool ? View.VISIBLE : View.INVISIBLE);
                tvListaPublicacionesVacia.setText("No se encontraron ventas");
                ivListaPublicacionesVacia.setImageResource(R.drawable.baseline_error_outline_24);
            }
        });

        viewModel.leerPublicacionesUsuario(usuarioActual);
        return root;
    }
}
