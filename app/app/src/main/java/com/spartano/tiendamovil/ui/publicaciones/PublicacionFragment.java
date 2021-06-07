package com.spartano.tiendamovil.ui.publicaciones;

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
import com.spartano.tiendamovil.model.Publicacion;
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

        viewModel.getPublicacionMutable().observe(getViewLifecycleOwner(), new Observer<Publicacion>() {
            @Override
            public void onChanged(Publicacion p) {
                publicacion = p;
                inicializarVista(root);
            }
        });
        viewModel.getErrorMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.leerPublicacion((Publicacion)getArguments().getSerializable("publicacion"));
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
        vpa.addFragment(new TabReseñasFragment(publicacion), "Reseñas");

        viewPage.setAdapter(vpa);
        tabLayout.setupWithViewPager(viewPage);
    }
}
