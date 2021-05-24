package com.spartano.tiendamovil.ui.publicaciones;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Publicacion;

public class PublicacionFragment extends Fragment {

    private PublicacionViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(PublicacionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_publicacion, container, false);

        ((TextView)root.findViewById(R.id.tvPrueba)).setText(((Publicacion)getArguments().getSerializable("publicacion")).getTitulo());

        inicializarVista(root);
        return root;
    }

    private void inicializarVista(View root){

    }
}