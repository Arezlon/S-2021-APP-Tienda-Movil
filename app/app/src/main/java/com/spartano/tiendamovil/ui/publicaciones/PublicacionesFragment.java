package com.spartano.tiendamovil.ui.publicaciones;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.ui.nuevaPublicacion.NuevaPublicacionViewModel;

import java.util.ArrayList;
import java.util.List;

public class PublicacionesFragment extends Fragment {
    private ListView lvPublicaciones;
    private PublicacionesViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(PublicacionesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_publicaciones, container, false);

        inicializarVista(root);
        viewModel.getPublicacionesMutable().observe(getViewLifecycleOwner(), new Observer<List<Publicacion>>() {
            @Override
            public void onChanged(List<Publicacion> publicaciones) {
                ArrayList<Publicacion> arrayList = new ArrayList<Publicacion>(publicaciones);
                ArrayAdapter<Publicacion> adapter = new PublicacionesListAdapter(getContext(),
                        R.layout.list_item_publicacion, arrayList,
                        getLayoutInflater(), true);
                lvPublicaciones.setAdapter(adapter);
            }
        });

        viewModel.getErrorMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.leerMisPublicaciones();
        return root;
    }

    private void inicializarVista(View root) {
        lvPublicaciones = root.findViewById(R.id.lvPublicaciones);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_nueva_publicacion);
            }
        });
    }
}