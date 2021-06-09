package com.spartano.tiendamovil;


import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.spartano.tiendamovil.model.Usuario;

import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MenuNavegacionActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView tvMenuUsuario, notificaciones;
    private Usuario usuario;
    private MenuNavegacionViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_navegacion);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        notificaciones = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_notificaciones));

        viewModel = new ViewModelProvider(this).get(MenuNavegacionViewModel.class);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_busqueda_avanzada, R.id.nav_nueva_publicacion, R.id.nav_mi_perfil, R.id.nav_publicaciones, R.id.nav_fondos, R.id.nav_notificaciones, R.id.nav_salir)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        viewModel.getUsuarioMutable().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuariom) {
                try{
                    usuario = usuariom;
                    tvMenuUsuario = findViewById(R.id.tvMenuUsuario);
                    tvMenuUsuario.setText(usuario.getApellido()+" "+usuario.getNombre()+" | $"+(int)usuario.getFondos());
                }
                catch (Exception e){
                    viewModel.ObtenerUsuario();
                }

            }
        });

        viewModel.getTotalNotificacionesMutable().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                try{
                    notificaciones.setGravity(Gravity.CENTER_VERTICAL);
                    notificaciones.setTypeface(null, Typeface.BOLD);
                    notificaciones.setTextColor(getResources().getColor(R.color.color_principal));
                    notificaciones.setText(s);
                }
                catch(Exception e){
                    viewModel.ObtenerCantidadNotificaciones();
                }

            }
        });

        actualizarDatosUsuario();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navegacion, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void actualizarDatosUsuario(){
        viewModel.ObtenerUsuario();
        viewModel.ObtenerCantidadNotificaciones();
    }
}