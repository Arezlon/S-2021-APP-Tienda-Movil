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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spartano.tiendamovil.MenuNavegacionActivity;
import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Notificacion;

import java.util.ArrayList;
import java.util.List;

public class NotificacionesFragment extends Fragment {

    private NotificacionesViewModel viewModel;
    private ListView lvNotificaciones;
    private TextView tvListaNotificacionesVacia;
    private ImageView ivListaNotificacionesVacia;

    public static NotificacionesFragment newInstance() {
        return new NotificacionesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(NotificacionesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notificaciones, container, false);
        lvNotificaciones = root.findViewById(R.id.lvNotificaciones);

        viewModel.getListaNotificacionesVaciaMutable().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                tvListaNotificacionesVacia = root.findViewById(R.id.tvListaNotificacionesVacia);
                ivListaNotificacionesVacia = root.findViewById(R.id.ivListaNotificacionesVacia);
                tvListaNotificacionesVacia.setVisibility(bool ? View.VISIBLE : View.INVISIBLE);
                ivListaNotificacionesVacia.setVisibility(bool ? View.VISIBLE : View.INVISIBLE);
                tvListaNotificacionesVacia.setText("No se encontraron notificaciones");
                ivListaNotificacionesVacia.setImageResource(R.drawable.baseline_error_outline_24);
            }
        });

        viewModel.getErrorMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getNotificacionesMutable().observe(getViewLifecycleOwner(), new Observer<List<Notificacion>>() {
            @Override
            public void onChanged(List<Notificacion> notificaciones) {
                ((MenuNavegacionActivity)getActivity()).actualizarDatosUsuario();
                ArrayList<Notificacion> arrayList = new ArrayList<Notificacion>(notificaciones);
                ArrayAdapter<Notificacion> adapter = new NotificacionesListAdapter(viewModel,
                        getContext(),
                        R.layout.list_item_notificacion, arrayList,
                        getLayoutInflater());
                lvNotificaciones.setAdapter(adapter);
            }
        });

        viewModel.ObtenerNotificaciones();
        return root;
    }

}