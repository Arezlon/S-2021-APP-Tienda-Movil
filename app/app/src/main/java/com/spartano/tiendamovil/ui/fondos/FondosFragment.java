package com.spartano.tiendamovil.ui.fondos;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Transaccion;
import com.spartano.tiendamovil.model.Usuario;

public class FondosFragment extends Fragment {

    private FondosViewModel viewModel;
    private Button btIngresarFondos;
    private EditText etIngresarFondos;
    private Spinner spTipoIngreso;
    private TextView tvFondos;
    private Usuario usuarioActual;

    public static FondosFragment newInstance() {
        return new FondosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(FondosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_fondos, container, false);

        viewModel.getUsuarioMutable().observe(getViewLifecycleOwner(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                usuarioActual = usuario;
                inicializarVista(root);
            }
        });

        viewModel.getErrorCargaMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
                btIngresarFondos.setEnabled(true);
                btIngresarFondos.setText("Ingresar fondos");
            }
        });
        viewModel.getCargaCorrectaMutable().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Toast.makeText(getContext(),"$"+etIngresarFondos.getText()+" fondos cargados correctamente",Toast.LENGTH_LONG).show();
                btIngresarFondos.setEnabled(true);
                btIngresarFondos.setText("Ingresar fondos");
                viewModel.ObtenerUsuario();
            }
        });
        viewModel.ObtenerUsuario();
        return root;
    }

    private void inicializarVista(View root) {
        btIngresarFondos = root.findViewById(R.id.btIngresarFondos);
        etIngresarFondos = root.findViewById(R.id.etIngresarFondos);
        spTipoIngreso = root.findViewById(R.id.spTipoIngreso);
        tvFondos = root.findViewById(R.id.tvFondos);

        tvFondos.setText(""+usuarioActual.getFondos());
        btIngresarFondos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btIngresarFondos.setEnabled(false);
                btIngresarFondos.setText("Cargando...");
                Transaccion transaccion = new Transaccion();
                transaccion.setImporte(Float.parseFloat(etIngresarFondos.getText().toString()));
                transaccion.setTipo(1);
                transaccion.setMetodoPagoCarga(1); //Desplegable
                viewModel.verificarCargaFondos(transaccion);
            }
        });
    }

}