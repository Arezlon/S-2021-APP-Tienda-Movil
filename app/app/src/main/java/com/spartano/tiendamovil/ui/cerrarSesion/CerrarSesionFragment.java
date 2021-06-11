package com.spartano.tiendamovil.ui.cerrarSesion;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spartano.tiendamovil.MainActivity;
import com.spartano.tiendamovil.MenuNavegacionActivity;
import com.spartano.tiendamovil.R;

public class CerrarSesionFragment extends Fragment {

    private boolean cerrar = false;
    private CerrarSesionViewModel viewModel;

    public static CerrarSesionFragment newInstance() {
        return new CerrarSesionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(CerrarSesionViewModel.class);
        return inflater.inflate(R.layout.fragment_cerrar_sesion, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        new AlertDialog.Builder(getActivity())
                .setTitle("Cerrar sesión")
                .setMessage("¿Seguro que quiere cerrar sesión?")
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        startActivity(new Intent(getActivity(), cerrar ? MainActivity.class : MenuNavegacionActivity.class));
                    }
                })
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cerrar = true;
                        viewModel.cerrarSesion();
                    }
                }).show();
    }

}