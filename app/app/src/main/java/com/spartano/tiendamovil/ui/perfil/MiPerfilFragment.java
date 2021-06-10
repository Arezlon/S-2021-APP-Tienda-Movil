package com.spartano.tiendamovil.ui.perfil;

import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.PerfilDataResponse;
import com.spartano.tiendamovil.model.Usuario;

public class MiPerfilFragment extends Fragment {

    private MiPerfilViewModel viewModel;
    private PerfilDataResponse datosUsuario;

    public static MiPerfilFragment newInstance() {
        return new MiPerfilFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(MiPerfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        /*viewModel.getUsuarioMutable().observe(getViewLifecycleOwner(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                usuarioActual = usuario;
                inicializarVista(root);
            }
        });*/

        viewModel.getDatosUsuarioMutable().observe(getViewLifecycleOwner(), new Observer<PerfilDataResponse>() {
            @Override
            public void onChanged(PerfilDataResponse perfilDataResponse) {
                datosUsuario = perfilDataResponse;
                inicializarVista(root);
            }
        });

        viewModel.getErrorMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.obtenerDatosUsuario();
        return root;
    }

    private void inicializarVista(View root) {
        ViewPager viewPage = root.findViewById(R.id.viewPage);
        AppBarLayout appBar = root.findViewById(R.id.appBar);
        TabLayout tabLayout = new TabLayout(requireContext());
        appBar.removeAllViews();
        appBar.addView(tabLayout);

        ViewPageAdapter vpa = new ViewPageAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpa.addFragment(new TabMiPerfilFragment(datosUsuario), "Mi perfil");
        vpa.addFragment(new TabComprasFragment(), "Mis compras");
        vpa.addFragment(new TabVentasFragment(), "Mis ventas");

        viewPage.setAdapter(vpa);
        tabLayout.setupWithViewPager(viewPage);
    }

}