package com.spartano.tiendamovil.ui.perfil;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Usuario;

public class TabPerfilFragment extends Fragment {

    private Button btEditar;
    private Usuario usuarioActual;

    public TabPerfilFragment(Usuario usuario){
        this.usuarioActual = usuario;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_tab_perffil, container, false);
        inicializarVista(root);
        return root;
    }

    private void inicializarVista(View root) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        btEditar = root.findViewById(R.id.btEditar);
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