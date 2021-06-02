package com.spartano.tiendamovil.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.spartano.tiendamovil.MenuNavegacionActivity;
import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class InicioFragment extends Fragment {

    private InicioViewModel viewModel;
    RecyclerView rvPrueba;
    private List<Publicacion> destacadas;
    private Usuario usuarioActual;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(InicioViewModel.class);
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);

        viewModel.getUsuarioMutable().observe(getViewLifecycleOwner(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                usuarioActual = usuario;
                ((MenuNavegacionActivity)getActivity()).actualizarDatosUsuario();
            }
        });

        viewModel.getPublicacionesDestacadasMutable().observe(getViewLifecycleOwner(), new Observer<List<Publicacion>>() {
            @Override
            public void onChanged(List<Publicacion> publicaciones) {
                destacadas = publicaciones;
                rvPrueba.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

                PublicacionesRecyclerAdapter adapter = new PublicacionesRecyclerAdapter(destacadas, getContext());
                rvPrueba.setAdapter(adapter);
            }
        });

        viewModel.getErrorMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        inicializarVista(root);
        viewModel.ObtenerUsuario();
        viewModel.leerPublicacionesDestacadas();
        return root;
    }

    private void inicializarVista(View root) {
        rvPrueba = root.findViewById(R.id.rvPrueba);
        
        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_nueva_publicacion);
            }
        });
    }
}