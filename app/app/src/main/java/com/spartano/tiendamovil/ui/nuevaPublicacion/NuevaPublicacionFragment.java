package com.spartano.tiendamovil.ui.nuevaPublicacion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.spartano.tiendamovil.R;

public class NuevaPublicacionFragment extends Fragment {

    private NuevaPublicacionViewModel nuevaPublicacionViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        nuevaPublicacionViewModel =
                new ViewModelProvider(this).get(NuevaPublicacionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_nueva_publicacion, container, false);
        /*final TextView textView = root.findViewById(R.id.nueva_publicacion);
        nuevaPublicacionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}