package com.spartano.tiendamovil.ui.publicaciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Comentario;
import com.spartano.tiendamovil.model.Publicacion;

import java.util.List;

public class TabComentariosFragment extends Fragment {
    public TabComentariosViewModel viewModel;

    private ListView lvComentarios;
    private Button btEnviarComentario;
    private EditText etComentario;
    private Publicacion publicacion;

    private boolean publicacionEsMia;

    public TabComentariosFragment(Publicacion publicacion) { this.publicacion = publicacion; }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(TabComentariosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tab_publicacion_comentarios, container, false);

        viewModel.getComentariosMutable().observe(getViewLifecycleOwner(), new Observer<List<Comentario>>() {
            @Override
            public void onChanged(List<Comentario> comentarios) {
                ArrayAdapter<Comentario> adapter = new ComentariosListAdapter(getContext(), R.layout.list_item_comentario, comentarios, getLayoutInflater(), publicacionEsMia, viewModel);
                lvComentarios.setAdapter(adapter);

                btEnviarComentario.setEnabled(!publicacionEsMia);
                etComentario.setEnabled(!publicacionEsMia);
                btEnviarComentario.setText("ENVIAR");
                etComentario.setText("");
            }
        });

        viewModel.getPublicacionEsMia().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                publicacionEsMia = bool;
                viewModel.leerComentarios(publicacion.getId());

                btEnviarComentario.setEnabled(!publicacionEsMia);
                etComentario.setEnabled(!publicacionEsMia);
            }
        });

        viewModel.comprobarUsuario(publicacion.getUsuarioId());
        inicializarVista(root);
        return root;
    }

    private void inicializarVista(View root) {
        lvComentarios = root.findViewById(R.id.lvComentarios);
        btEnviarComentario = root.findViewById(R.id.btEnviarComentario);
        etComentario = root.findViewById(R.id.etComentario);

        btEnviarComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comentario comentario = new Comentario();
                comentario.setPregunta(etComentario.getText().toString());
                comentario.setPublicacionId(publicacion.getId());
                comentario.setPublicacion(publicacion);

                viewModel.crearComentario(comentario);
                btEnviarComentario.setEnabled(false);
                btEnviarComentario.setText("Enviando...");
            }
        });
    }
}
