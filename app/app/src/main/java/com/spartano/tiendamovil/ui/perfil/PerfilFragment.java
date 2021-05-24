package com.spartano.tiendamovil.ui.perfil;

import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Usuario;
import com.spartano.tiendamovil.request.ApiClient;
import com.spartano.tiendamovil.ui.inicio.InicioViewModel;

public class PerfilFragment extends Fragment {

    private PerfilViewModel viewModel;
    private Usuario usuarioActual;

    public static PerfilFragment newInstance() {
        return new PerfilFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(PerfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        viewModel.getUsuarioMutable().observe(getViewLifecycleOwner(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                usuarioActual = usuario;
                inicializarVista(root);
            }
        });
        viewModel.ObtenerUsuario();
        return root;
    }

    private void inicializarVista(View root) {
        ViewPager viewPage = root.findViewById(R.id.viewPage);
        AppBarLayout appBar = root.findViewById(R.id.appBar);
        TabLayout tabLayout = new TabLayout(requireContext());
        appBar.removeAllViews();
        appBar.addView(tabLayout);

        ViewPageAdapter vpa = new ViewPageAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpa.addFragment(new TabPerfilFragment(usuarioActual), "Mi perfil");
        vpa.addFragment(new TabComprasFragment(), "Mis compras");
        vpa.addFragment(new TabVentasFragment(), "Mis ventas");

        viewPage.setAdapter(vpa);
        tabLayout.setupWithViewPager(viewPage);
    }

}