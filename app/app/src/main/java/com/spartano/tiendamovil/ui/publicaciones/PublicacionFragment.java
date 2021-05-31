package com.spartano.tiendamovil.ui.publicaciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.ui.perfil.TabComprasFragment;
import com.spartano.tiendamovil.ui.perfil.TabPerfilFragment;
import com.spartano.tiendamovil.ui.perfil.TabVentasFragment;
import com.spartano.tiendamovil.ui.perfil.ViewPageAdapter;

public class PublicacionFragment extends Fragment {
    private PublicacionViewModel viewModel;
    private Publicacion publicacion;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(PublicacionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_publicacion, container, false);

        publicacion = (Publicacion)getArguments().getSerializable("publicacion");
        inicializarVista(root);
        return root;
    }

    private void inicializarVista(View root) {
        ViewPager viewPage = root.findViewById(R.id.viewPage);
        AppBarLayout appBar = root.findViewById(R.id.appBar);
        TabLayout tabLayout = new TabLayout(requireContext());
        appBar.removeAllViews();
        appBar.addView(tabLayout);

        ViewPageAdapter vpa = new ViewPageAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpa.addFragment(new TabPublicacionFragment(publicacion), "Publicación");
        vpa.addFragment(new TabComentariosFragment(publicacion), "Preguntas");

        viewPage.setAdapter(vpa);
        tabLayout.setupWithViewPager(viewPage);
    }
}
