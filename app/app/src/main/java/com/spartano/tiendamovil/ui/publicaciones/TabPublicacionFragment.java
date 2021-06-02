package com.spartano.tiendamovil.ui.publicaciones;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.model.PublicacionImagen;
import com.spartano.tiendamovil.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

public class TabPublicacionFragment extends Fragment {

    private TabPublicacionViewModel viewModel;
    private Publicacion publicacion;

    private Button btAgregarImagen, btImagenAnterior, btImagenSiguiente, btEliminarImagen, btDestacarImagen, btPublicacionComprar;
    private ImageView ivPreviewImagen;
    private EditText etPublicacionCantidad;
    private TextView tvPublicacionTitulo, tvPublicacionPrecio, tvPublicacionStock, tvPublicacionCategoria, tvPublicacionDescripcion, tvPublicacionTipo;


    private List<PublicacionImagen> imagenes;
    private int pos = 0;

    public TabPublicacionFragment (Publicacion publicacion) { this.publicacion = publicacion; }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(TabPublicacionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tab_publicacion, container, false);

        // Si la publicacion tiene imagenes, mostrarlas el el carrusel
        viewModel.getImagenesMutable().observe(getViewLifecycleOwner(), new Observer<List<PublicacionImagen>>() {
            @Override
            public void onChanged(List<PublicacionImagen> publicacionImagenes) {
                imagenes = publicacionImagenes;
                btImagenAnterior.setEnabled(imagenes.size() > 1);
                btImagenSiguiente.setEnabled(imagenes.size() > 1);
                pos = 0;
                setImagen(pos);
                btEliminarImagen.setEnabled(true);
            }
        });

        viewModel.getErrorMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                btPublicacionComprar.setEnabled(true);
                btPublicacionComprar.setText("Comprar");
            }
        });

        // Si no hay imagenes cargadas o si se borraron todas las imagenes
        viewModel.getSinImagenesMutable().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                imagenes = new ArrayList<>();
                pos = 0;
                ivPreviewImagen.setImageDrawable(null);
                btDestacarImagen.setEnabled(false);
                btEliminarImagen.setEnabled(false);
                btImagenAnterior.setEnabled(false);
                btImagenSiguiente.setEnabled(false);
            }
        });

        viewModel.getPublicacionEsMia().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                btAgregarImagen.setVisibility(View.VISIBLE);
                btEliminarImagen.setVisibility(View.VISIBLE);
                btDestacarImagen.setVisibility(View.VISIBLE);
                btPublicacionComprar.setEnabled(false);
            }
        });

        viewModel.getCompraMutable().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Navigation.findNavController((Activity)getContext(), R.id.nav_host_fragment).navigate(R.id.nav_compra);
            }
        });

        inicializarVista(root);
        viewModel.leerImagenesPublicacion(publicacion.getId());
        viewModel.comprobarUsuario(publicacion.getUsuarioId());
        return root;
    }

    private void inicializarVista(View root){
        btAgregarImagen = root.findViewById(R.id.btAgregarImagen);
        btImagenAnterior = root.findViewById(R.id.btImagenAnterior);
        btImagenSiguiente = root.findViewById(R.id.btImagenSiguiente);
        btEliminarImagen = root.findViewById(R.id.btEliminarImagen);
        btDestacarImagen = root.findViewById(R.id.btDestacarImagen);
        ivPreviewImagen = root.findViewById(R.id.ivPreviewImagen);
        btPublicacionComprar = root.findViewById(R.id.btPublicacionComprar);

        tvPublicacionTitulo = root.findViewById(R.id.tvPublicacionTitulo);
        tvPublicacionPrecio = root.findViewById(R.id.tvPublicacionPrecio);
        tvPublicacionStock = root.findViewById(R.id.tvPublicacionStock);
        tvPublicacionCategoria = root.findViewById(R.id.tvPublicacionCategoria);
        tvPublicacionDescripcion = root.findViewById(R.id.tvPublicacionDescripcion);
        tvPublicacionTipo = root.findViewById(R.id.tvPublicacionTipo);
        etPublicacionCantidad = root.findViewById(R.id.etPublicacionCantidad);

        tvPublicacionTitulo.setText(publicacion.getTitulo());
        tvPublicacionPrecio.setText("$"+publicacion.getPrecio());
        tvPublicacionStock.setText("("+publicacion.getStock()+" disponible/s)");
        tvPublicacionCategoria.setText(publicacion.getCategoriaNombre());
        tvPublicacionDescripcion.setText(publicacion.getDescripcion());
        tvPublicacionTipo.setText(publicacion.getTipoNombre());

        // Abrir galería para seleccionar una o muchas imágenes
        btAgregarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                i.setAction(Intent.ACTION_GET_CONTENT);

                // Cuando el usuario termine de elegir las imágenes se llama al onActivityResult con los resultados de la activity
                startActivityForResult(Intent.createChooser(i, "Imagenes"), 200);
            }
        });

        // Imágen anterior/izquierda
        btImagenAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = (pos > 0) ? (pos - 1) : (imagenes.size() - 1);
                setImagen(pos);
            }
        });

        // Imágen siguiente/derecha
        btImagenSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = (pos < imagenes.size() - 1) ? (pos + 1) : 0;
                setImagen(pos);
            }
        });

        // Eliminar imágen
        btEliminarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Eliminar imágen")
                        .setMessage("¿Seguro que quiere eliminar esta imágen?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Si, eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.eliminarImagen(imagenes.get(pos));
                            }
                        }).show();
            }
        });

        // Destacar imágen/marcar como favorita
        btDestacarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Destacar imágen")
                        .setMessage("¿Quiere marcar esta imágen como destacada? La imagen destacada de una publicacion es la que se muestra en los listados como representacion del producto.")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.destacarImagen(imagenes.get(pos));
                            }
                        }).show();
            }
        });

        btPublicacionComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPublicacionComprar.setEnabled(false);
                btPublicacionComprar.setText("Cargando...");
                int cantidad = Integer.parseInt(etPublicacionCantidad.getText().toString());
                viewModel.comprobarFondos(publicacion, cantidad);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.recibirImagenesGaleria(requestCode, resultCode, data, publicacion);
    }

    private void setImagen(int posicion) {
        Glide.with(getContext())
                .load(ApiClient.getPath()+imagenes.get(posicion).getDireccion())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(ivPreviewImagen);
        btDestacarImagen.setEnabled(imagenes.get(posicion).getEstado() != 2);
    }
}