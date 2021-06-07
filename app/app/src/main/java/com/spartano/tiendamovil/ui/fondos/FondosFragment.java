package com.spartano.tiendamovil.ui.fondos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
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

import com.spartano.tiendamovil.MenuNavegacionActivity;
import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Transaccion;
import com.spartano.tiendamovil.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class FondosFragment extends Fragment {

    private FondosViewModel viewModel;
    private Button btIngresarFondos;
    private EditText etIngresarFondos;
    private TextView tvFondos, tvListaTransaccionesVacia;
    private Usuario usuarioActual;
    private ListView listHistorial;
    private ImageView ivListaTransaccionesVacia;

    public static FondosFragment newInstance() {
        return new FondosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(FondosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_fondos, container, false);

        viewModel.getListaTransaccionesVaciaMutable().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                tvListaTransaccionesVacia = root.findViewById(R.id.tvListaTransaccionesVacia);
                ivListaTransaccionesVacia = root.findViewById(R.id.ivListaTransaccionesVacia);
                tvListaTransaccionesVacia.setVisibility(bool ? View.VISIBLE : View.INVISIBLE);
                ivListaTransaccionesVacia.setVisibility(bool ? View.VISIBLE : View.INVISIBLE);
                tvListaTransaccionesVacia.setText("No se encontraron transacciones");
                ivListaTransaccionesVacia.setImageResource(R.drawable.baseline_error_outline_24);
            }
        });

        viewModel.getUsuarioMutable().observe(getViewLifecycleOwner(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                usuarioActual = usuario;
                inicializarVista(root);
                ((MenuNavegacionActivity)getActivity()).actualizarDatosUsuario();
                viewModel.leerHistorialTransacciones();
            }
        });

        viewModel.getErrorCargaMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
                btIngresarFondos.setEnabled(true);
                btIngresarFondos.setText("Ingresar");
            }
        });

        viewModel.getErrorMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                // TODO: cambiar este toast por un TextView que se muestre en lugar del ListView
                Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getCargaCorrectaMutable().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Toast.makeText(getContext(),"$"+etIngresarFondos.getText()+" cargados correctamente",Toast.LENGTH_LONG).show();
                btIngresarFondos.setEnabled(true);
                btIngresarFondos.setText("Ingresar");
                etIngresarFondos.setText("");
                viewModel.ObtenerUsuario();
            }
        });

        viewModel.getTransaccionesMutable().observe(getViewLifecycleOwner(), new Observer<List<Transaccion>>() {
            @Override
            public void onChanged(List<Transaccion> transacciones) {
                ArrayList<Transaccion> arrayList = new ArrayList<Transaccion>(transacciones);
                ArrayAdapter<Transaccion> adapter = new TransaccionesListAdapter(getContext(),
                        R.layout.list_item_transaccion, arrayList,
                        getLayoutInflater());
                listHistorial.setAdapter(adapter);
            }
        });

        viewModel.ObtenerUsuario();
        return root;
    }

    private void inicializarVista(View root) {
        btIngresarFondos = root.findViewById(R.id.btIngresarFondos);
        etIngresarFondos = root.findViewById(R.id.etIngresarFondos);
        tvFondos = root.findViewById(R.id.tvFondos);
        listHistorial = root.findViewById(R.id.listHistorial);

        tvFondos.setText("$"+usuarioActual.getFondos());
        btIngresarFondos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btIngresarFondos.setEnabled(false);
                btIngresarFondos.setText("Cargando...");
                Transaccion transaccion = new Transaccion();
                try {
                    transaccion.setImporte(Float.parseFloat(etIngresarFondos.getText().toString()));
                }
                catch (NumberFormatException e) {
                    transaccion.setImporte(99);
                }
                transaccion.setTipo(1);
                viewModel.verificarCargaFondos(transaccion);
            }
        });
    }

}