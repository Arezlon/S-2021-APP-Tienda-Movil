package com.spartano.tiendamovil.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Publicacion;

import java.util.ArrayList;
import java.util.List;

public class InicioFragment extends Fragment {

    private InicioViewModel inicioViewModel;
    RecyclerView rvPrueba;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inicioViewModel =
                new ViewModelProvider(this).get(InicioViewModel.class);
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);


        rvPrueba = root.findViewById(R.id.rvPrueba);
        rvPrueba.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        List<Publicacion> lista = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            lista.add(new Publicacion());

        PublicacionesRecyclerAdapter adapter = new PublicacionesRecyclerAdapter(lista);
        rvPrueba.setAdapter(adapter);

        return root;
    }
}