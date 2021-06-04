package com.spartano.tiendamovil.ui.notificaciones;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Notificacion;

import java.util.ArrayList;
import java.util.List;

public class NotificacionesFragment extends Fragment {

    private NotificacionesViewModel viewModel;
    private ListView lvNotificaciones;

    public static NotificacionesFragment newInstance() {
        return new NotificacionesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(NotificacionesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notificaciones, container, false);
        lvNotificaciones = root.findViewById(R.id.lvNotificaciones);

        viewModel.getNotificacionesMutable().observe(getViewLifecycleOwner(), new Observer<List<Notificacion>>() {
            @Override
            public void onChanged(List<Notificacion> notificaciones) {
                ArrayList<Notificacion> arrayList = new ArrayList<Notificacion>(notificaciones);
                ArrayAdapter<Notificacion> adapter = new NotificacionesListAdapter(getContext(),
                        R.layout.list_item_notificacion, arrayList,
                        getLayoutInflater());
                lvNotificaciones.setAdapter(adapter);
            }
        });

        viewModel.ObtenerNotificaciones();
        return root;
    }

}