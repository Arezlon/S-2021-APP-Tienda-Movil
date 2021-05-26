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
import android.widget.EditText;
import android.widget.Toast;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Usuario;

public class EditarPerfilFragment extends Fragment {

    private EditarPerfilViewModel viewModel;
    private Button btGuardarCambios;
    private EditText etEditarNombre, etEditarApellido, etEditarTelefono, etEditarDni, etEditarMail, etEditarDireccion, etEditarLocalidad, etEditarProvincia, etEditarPais;
    private Usuario usuario;

    public static PerfilFragment newInstance() {
        return new PerfilFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(EditarPerfilViewModel.class);
        usuario = (Usuario)getArguments().getSerializable("usuarioActual");
        View root = inflater.inflate(R.layout.fragment_editar_perfil, container, false);
        viewModel.getErrorVerificacionMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
                btGuardarCambios.setEnabled(true);
                btGuardarCambios.setText("Guardar cambios");
            }
        });
        viewModel.getEdicionCorrectaMutable().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Toast.makeText(getContext(),"Datos editados correctamente",Toast.LENGTH_LONG).show();
                btGuardarCambios.setEnabled(true);
                btGuardarCambios.setText("Guardar cambios");
            }
        });
        inicializarVista(root);
        return root;
    }

    private void inicializarVista(View root) {
        btGuardarCambios = root.findViewById(R.id.btGuardarCambios);
        etEditarNombre = root.findViewById(R.id.etEditarNombre);
        etEditarApellido = root.findViewById(R.id.etEditarApellido);
        etEditarTelefono = root.findViewById(R.id.etEditarTelefono);
        etEditarDni = root.findViewById(R.id.etEditarDni);
        etEditarMail = root.findViewById(R.id.etEditarMail);
        etEditarDireccion = root.findViewById(R.id.etEditarDireccion);
        etEditarLocalidad = root.findViewById(R.id.etEditarLocalidad);
        etEditarProvincia = root.findViewById(R.id.etEditarProvincia);
        etEditarPais = root.findViewById(R.id.etEditarPais);

        etEditarNombre.setText(usuario.getNombre());
        etEditarApellido.setText(usuario.getApellido());
        etEditarTelefono.setText(usuario.getTelefono());
        etEditarDni.setText(usuario.getDni());
        etEditarMail.setText(usuario.getEmail());

        try {
            etEditarDireccion.setText(usuario.getDireccion().toString());
            etEditarLocalidad.setText(usuario.getLocalidad().toString());
            etEditarProvincia.setText(usuario.getProvinicia().toString());
            etEditarPais.setText(usuario.getPais().toString());
        }catch(NullPointerException e){
            Log.d("salida", "Los campos de ubicación están vacíos (antes de entrar a la edición)");
        }

        btGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btGuardarCambios.setEnabled(false);
                btGuardarCambios.setText("Cargando...");
                Usuario usuarioEditado = new Usuario();
                usuarioEditado.setNombre(etEditarNombre.getText().toString());
                usuarioEditado.setApellido(etEditarApellido.getText().toString());
                usuarioEditado.setTelefono(etEditarTelefono.getText().toString());
                usuarioEditado.setDni(etEditarDni.getText().toString());
                usuarioEditado.setEmail(etEditarMail.getText().toString());

                try {
                    usuarioEditado.setDireccion(etEditarDireccion.getText().toString());
                    usuarioEditado.setLocalidad(etEditarLocalidad.getText().toString());
                    usuarioEditado.setProvinicia(etEditarProvincia.getText().toString());
                    usuarioEditado.setPais(etEditarPais.getText().toString());
                }catch(NullPointerException e){
                    Log.d("salida", "Los campos de ubicación están vacíos (al intentar guardar los cambios)");
                }
                viewModel.verificarEdicion(usuarioEditado, usuario);
            }
        });

    }

}