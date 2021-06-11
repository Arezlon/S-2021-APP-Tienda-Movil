package com.spartano.tiendamovil.ui.perfil;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.PerfilDataResponse;
import com.spartano.tiendamovil.model.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TabMiPerfilFragment extends Fragment {

    private Button btEditar;
    private PerfilDataResponse datosUsuario;
    private TextView tvNombreCompleto, tvUbicacion, tvFechaRegistro, tvEmailContacto, tvValoracion, tvCantidadReseñas, tvCantidadVentas;
    private RatingBar rbValoracion;

    public TabMiPerfilFragment(PerfilDataResponse datosUsuario){
        this.datosUsuario = datosUsuario;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_tab_perfil, container, false);
        inicializarVista(root);
        return root;
    }

    private void inicializarVista(View root) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        btEditar = root.findViewById(R.id.btEditar);
        tvNombreCompleto = root.findViewById(R.id.tvNombreCompleto);
        tvUbicacion = root.findViewById(R.id.tvUbicacion);
        tvFechaRegistro = root.findViewById(R.id.tvFechaRegistro);
        tvEmailContacto = root.findViewById(R.id.tvEmailContacto);
        tvValoracion = root.findViewById(R.id.tvValoracion);
        tvCantidadReseñas = root.findViewById(R.id.tvCantidadReseñas);
        tvCantidadVentas = root.findViewById(R.id.tvCantidadVentas);
        rbValoracion = root.findViewById(R.id.rbValoracion);

        tvNombreCompleto.setText(datosUsuario.getUsuario().getApellido()+" "+datosUsuario.getUsuario().getNombre());
        try {
            tvUbicacion.setText(datosUsuario.getUsuario().getDireccion().toString() + " - " + datosUsuario.getUsuario().getLocalidad().toString()+", "+datosUsuario.getUsuario().getProvinicia().toString()+", "+datosUsuario.getUsuario().getPais().toString());
        }catch(NullPointerException e){
            tvUbicacion.setText("Ubicación del usuario indefinida");
        }

        String fecha = datosUsuario.getUsuario().getCreacion().substring(0,10);
        //String hora = usuarioActual.getCreacion().substring(11);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyy");
        //SimpleDateFormat formatter3 = new SimpleDateFormat("HH:mm:ss");
        try {
            Date fechaConvertida = formatter.parse(fecha);
            //Date horaConvertida = formatter3.parse(hora);
            tvFechaRegistro.setText("Miembro desde: " + formatter2.format(fechaConvertida));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvEmailContacto.setText(datosUsuario.getUsuario().getEmail());
        //Calcular valoracion de vendedor (basado en resñas)
        //tvValoracion.setText()
        rbValoracion.setRating(datosUsuario.getValoracion()/2/10);

        tvValoracion.setText("Reputacion del vendedor: " + datosUsuario.getReputacion() + " (" + datosUsuario.getValoracion() + "%)");
        tvCantidadReseñas.setText(""+datosUsuario.getCantidadReseñas());
        tvCantidadVentas.setText(""+datosUsuario.getCantidadVentas());


        btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("usuarioActual", datosUsuario.getUsuario());
                navController.navigate(R.id.nav_editar_perfil, bundle);
            }
        });
    }

}