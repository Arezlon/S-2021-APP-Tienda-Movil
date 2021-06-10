package com.spartano.tiendamovil.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.PerfilDataResponse;
import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.model.Usuario;
import com.spartano.tiendamovil.ui.publicaciones.PublicacionesFragment;

import java.util.List;

public class PerfilFragment extends Fragment {

    private PerfilViewModel viewModel;
    //private Usuario usuarioActual;
    private PerfilDataResponse datosUsuario;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(PerfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        viewModel.getErrorMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getDatosUsuarioMutable().observe(getViewLifecycleOwner(), new Observer<PerfilDataResponse>() {
            @Override
            public void onChanged(PerfilDataResponse perfilDataResponse) {
                datosUsuario = perfilDataResponse;
                inicializarVista(root);
            }
        });

        Usuario usuario = (Usuario)getArguments().getSerializable("usuario");
        viewModel.obtenerDatosUsuario(usuario.getId());
        return root;
    }

    private void inicializarVista(View root) {
        ViewPager viewPage = root.findViewById(R.id.viewPage);
        AppBarLayout appBar = root.findViewById(R.id.appBar);
        TabLayout tabLayout = new TabLayout(requireContext());
        appBar.removeAllViews();
        appBar.addView(tabLayout);

        ViewPageAdapter vpa = new ViewPageAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpa.addFragment(new TabPerfilFragment(datosUsuario), "Vendedor");
        vpa.addFragment(new TabPublicacionesFragment(datosUsuario.getUsuario()), "Publicaciones");

        viewPage.setAdapter(vpa);
        tabLayout.setupWithViewPager(viewPage);
    }
}
