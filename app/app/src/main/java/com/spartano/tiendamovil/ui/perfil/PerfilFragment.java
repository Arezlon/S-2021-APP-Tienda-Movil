package com.spartano.tiendamovil.ui.perfil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Usuario;
import com.spartano.tiendamovil.request.ApiClient;
import com.spartano.tiendamovil.ui.inicio.InicioViewModel;

public class PerfilFragment extends Fragment {

    private PerfilViewModel viewModel;
    private Button btEditar;
    private Usuario usuarioActual;

    public static PerfilFragment newInstance() {
        return new PerfilFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(PerfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        viewModel.getUsuarioMutable().observe(getViewLifecycleOwner(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                Log.d("salida","onchaged"+usuario.getApellido());
                btEditar.setEnabled(true);
                usuarioActual = usuario;
            }
        });
        inicializarVista(root);
        viewModel.ObtenerUsuario();
        return root;
    }

    private void inicializarVista(View root) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        btEditar = root.findViewById(R.id.btEditar);
        btEditar.setEnabled(false);
        btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("usuarioActual", usuarioActual);
                navController.navigate(R.id.nav_editar_perfil, bundle);
            }
        });

        }

}