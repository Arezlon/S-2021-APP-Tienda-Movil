package com.spartano.tiendamovil.ui.publicaciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.spartano.tiendamovil.model.Comentario;
import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.model.Reseña;

import java.util.List;

public class TabReseñasFragment extends Fragment {
    public TabReseñasViewModel viewModel;

    private ListView lvReseñas;
    private Publicacion publicacion;
    private TextView tvListaReseñasVacia;
    private ImageView ivListaReseñasVacia;

    //private boolean publicacionEsMia;

    public TabReseñasFragment(Publicacion publicacion) { this.publicacion = publicacion; }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(TabReseñasViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tab_publicacion_resenas, container, false);

        viewModel.getListaVaciaMutable().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                tvListaReseñasVacia = root.findViewById(R.id.tvListaReseñasVacia);
                ivListaReseñasVacia = root.findViewById(R.id.ivListaReseñasVacia);
                tvListaReseñasVacia.setVisibility(bool ? View.VISIBLE : View.INVISIBLE);
                ivListaReseñasVacia.setVisibility(bool ? View.VISIBLE : View.INVISIBLE);
                tvListaReseñasVacia.setText("No se encontraron reseñas");
                ivListaReseñasVacia.setImageResource(R.drawable.baseline_error_outline_24);
            }
        });

        viewModel.getErrorMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getReseñasMutable().observe(getViewLifecycleOwner(), new Observer<List<Reseña>>() {
            @Override
            public void onChanged(List<Reseña> reseñas) {
                ArrayAdapter<Reseña> adapter = new ReseñasListAdapter(getContext(), R.layout.list_item_resena, reseñas, getLayoutInflater());
                lvReseñas.setAdapter(adapter);
            }
        });

        viewModel.getPublicacionEsMia().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                viewModel.leerReseñas(publicacion.getId());
            }
        });

        viewModel.comprobarUsuario(publicacion.getUsuarioId());
        inicializarVista(root);
        return root;
    }

    private void inicializarVista(View root) {
        lvReseñas = root.findViewById(R.id.lvReseñas);
    }
}
