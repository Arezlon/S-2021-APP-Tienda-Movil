package com.spartano.tiendamovil.ui.publicaciones;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.spartano.tiendamovil.MenuNavegacionActivity;
import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Etiqueta;
import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.model.PublicacionEtiqueta;
import com.spartano.tiendamovil.model.PublicacionImagen;
import com.spartano.tiendamovil.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

public class TabPublicacionFragment extends Fragment {

    private TabPublicacionViewModel viewModel;
    private Publicacion publicacion;

    private Button btAgregarImagen, btImagenAnterior, btImagenSiguiente, btEliminarImagen, btDestacarImagen, btPublicacionComprar, btEditarPublicacion;
    private ImageView ivPreviewImagen;
    private EditText etPublicacionCantidad;
    private TextView tvPublicacionTitulo, tvPublicacionPrecio, tvPublicacionStock, tvPublicacionCategoria, tvPublicacionDescripcion, tvPublicacionTipo;
    private TextView tvNombreVendedor, tvReputacionVendedor;
    private Button btNuevaEtiqueta;
    private RecyclerView rvEtiquetas;

    private boolean publicacionEsMia = false;
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
            public void onChanged(Boolean bool) {
                publicacionEsMia = bool;
                inicializarVistaMia(root);
            }
        });

        viewModel.getCompraMutable().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                // Al realizar una compra, redirigir a los detalles
                ((MenuNavegacionActivity) getActivity()).actualizarDatosUsuario();
                NavController controller = Navigation.findNavController((Activity) getContext(), R.id.nav_host_fragment);
                controller.popBackStack();
                controller.navigate(R.id.nav_compra);
            }
        });

        viewModel.getEtiquetasMutable().observe(getViewLifecycleOwner(), new Observer<List<PublicacionEtiqueta>>() {
            @Override
            public void onChanged(List<PublicacionEtiqueta> etiquetas) {
                rvEtiquetas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                EtiquetasRecyclerAdapter adapter = new EtiquetasRecyclerAdapter(etiquetas, getContext(), viewModel, publicacionEsMia);
                rvEtiquetas.setAdapter(adapter);
            }
        });

        viewModel.getEdicionMutable().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                // Al editar la publicacion, redirigir a "mis publicaciones"
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_publicaciones);
            }
        });

        inicializarVista(root);
        viewModel.leerImagenesPublicacion(publicacion.getId());
        viewModel.comprobarUsuario(publicacion.getUsuarioId());
        viewModel.leerEtiquetas(publicacion.getId());
        return root;
    }

    // Solo se llama si la publicación es del usuario logueado
    private void inicializarVistaMia(View root) {
        // Mostrar elementos de gestión
        btAgregarImagen.setVisibility(View.VISIBLE);
        btEliminarImagen.setVisibility(View.VISIBLE);
        btDestacarImagen.setVisibility(View.VISIBLE);
        btEditarPublicacion.setVisibility(View.VISIBLE);
        btNuevaEtiqueta.setEnabled(true);
        btPublicacionComprar.setVisibility(View.INVISIBLE);

        // Abrir dialog de edición de publicación
        btEditarPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Editar publicación")
                        .setView(R.layout.dialog_editar_publicacion)
                        .show();

                // Elementos de la vista interna del dialog (R.layout.dialog_editar_publicacion)
                EditText etStock = dialog.findViewById(R.id.etStock);
                EditText etPrecio = dialog.findViewById(R.id.etPrecio);
                Switch swEstado = dialog.findViewById(R.id.swEstado);
                Button btConfirmarCambios = dialog.findViewById(R.id.btConfirmarCambios);

                etStock.setText(publicacion.getStock()+"");
                etPrecio.setText(publicacion.getPrecio()+"");
                swEstado.setChecked(publicacion.getEstado() == 1 ? true : false);

                btConfirmarCambios.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            int stock = 0 + Integer.valueOf(etStock.getText().toString());
                            float precio = 0 + Float.valueOf(etPrecio.getText().toString());
                            boolean estado = swEstado.isChecked();
                            viewModel.modificarPublicacion(publicacion, stock, precio, estado);
                            dialog.dismiss();
                        }catch (Exception e) {
                            Toast.makeText(getActivity(), "Error, los valores ingresados no son validos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Boton agregar etiquetas > abre un dialog que permite hacer alta de muchas etiquetas
        btNuevaEtiqueta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Las etiquetas creadas se guardan en esta lista, no se suben al servidor hasta que se toca el boton "confirmar"
                List<PublicacionEtiqueta> etiquetasPublicacion = new ArrayList<>();
                Dialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Agregar etiqueta")
                        .setView(R.layout.dialog_crear_etiqueta)
                        .setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.crearEtiquetas(etiquetasPublicacion, publicacion);
                                dialog.dismiss();
                            }
                        }).show();

                // Elementos de la vista interna del dialog (R.layout.dialog_crear_etiqueta)
                EditText etEtiqueta = dialog.findViewById(R.id.etEtiqueta);
                Button btAgregarEtiqueta = dialog.findViewById(R.id.btAgregarEtiqueta);
                RecyclerView rvEtiquetas = dialog.findViewById(R.id.rvEtiquetas);
                rvEtiquetas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

                // Cuando se agrega una etiqueta, sumarla a la lista y mostrarla en el RecyclerView
                btAgregarEtiqueta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Etiqueta e = new Etiqueta();
                        e.setNombre(etEtiqueta.getText().toString().toLowerCase());

                        PublicacionEtiqueta pe = new PublicacionEtiqueta();
                        pe.setEtiqueta(e);
                        pe.setPublicacion(publicacion);
                        pe.setPublicacionId(publicacion.getId());
                        etiquetasPublicacion.add(pe);

                        EtiquetasRecyclerAdapter adapter = new EtiquetasRecyclerAdapter(etiquetasPublicacion, getContext(), viewModel, false);
                        rvEtiquetas.setAdapter(adapter);

                        etEtiqueta.setText("");
                    }
                });
            }
        });

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
    }

    // Se llama siempre
    private void inicializarVista(View root){
        // Elementos visibles para todos los usuarios
        btImagenAnterior = root.findViewById(R.id.btImagenAnterior);
        btImagenSiguiente = root.findViewById(R.id.btImagenSiguiente);
        ivPreviewImagen = root.findViewById(R.id.ivPreviewImagen);
        tvPublicacionTitulo = root.findViewById(R.id.tvPublicacionTitulo);
        tvPublicacionPrecio = root.findViewById(R.id.tvPublicacionPrecio);
        tvPublicacionStock = root.findViewById(R.id.tvPublicacionStock);
        tvPublicacionCategoria = root.findViewById(R.id.tvPublicacionCategoria);
        tvPublicacionDescripcion = root.findViewById(R.id.tvPublicacionDescripcion);
        tvNombreVendedor = root.findViewById(R.id.tvNombreVendedor);
        tvReputacionVendedor = root.findViewById(R.id.tvReputacionVendedor);
        tvPublicacionTipo = root.findViewById(R.id.tvPublicacionTipo);

        // Elemento solo visible para los compradores
        btPublicacionComprar = root.findViewById(R.id.btPublicacionComprar);

        // Elementos solo visibles para el dueño de la publicación
        btAgregarImagen = root.findViewById(R.id.btAgregarImagen);
        btEliminarImagen = root.findViewById(R.id.btEliminarImagen);
        btDestacarImagen = root.findViewById(R.id.btDestacarImagen);
        btEditarPublicacion = root.findViewById(R.id.btEditarPublicacion);
        btNuevaEtiqueta = root.findViewById(R.id.btNuevaEtiqueta);
        rvEtiquetas = root.findViewById(R.id.rvEtiquetas);

        tvPublicacionTitulo.setText(publicacion.getTitulo());
        tvPublicacionPrecio.setText("$"+publicacion.getPrecio());
        tvPublicacionStock.setText("("+publicacion.getStock()+" disponible/s)");
        tvPublicacionCategoria.setText(publicacion.getCategoriaNombre());
        tvPublicacionDescripcion.setText(publicacion.getDescripcion());
        tvPublicacionTipo.setText(publicacion.getTipoNombre());
        tvNombreVendedor.setText(publicacion.getUsuario().getNombre() + " " + publicacion.getUsuario().getApellido());
        tvReputacionVendedor.setText("#"+publicacion.getUsuario().getId());


        // Redirigir al perfil del vendedor cuando se hace clic sobre su nombre
        tvNombreVendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putSerializable("usuario", publicacion.getUsuario());
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_perfil, b);
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

        // Abrir dialog de compra con selector de cantidad
        btPublicacionComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Realizar compra")
                        .setView(R.layout.dialog_comprar_publicacion)
                        .show();

                // Elementos del layout del dialog (dialog_comprar_publicacion)
                Button btComprar = dialog.findViewById(R.id.btComprar);
                EditText etCantidad = dialog.findViewById(R.id.etCantidadCompra);
                TextView tvTitulo = dialog.findViewById(R.id.tvTituloCompra);
                TextView tvPrecio = dialog.findViewById(R.id.tvPrecioTotalCompra);

                tvTitulo.setText(publicacion.getTitulo());
                tvPrecio.setText("$"+publicacion.getPrecio());
                // Cambiar el texto de tvPrecio cada vez que se cambia la cantidad (etCantidad)
                etCantidad.addTextChangedListener(new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void afterTextChanged(Editable s) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            int cantidad = Integer.parseInt("0"+s.toString());
                            tvPrecio.setText("$"+publicacion.getPrecio() * Math.min(cantidad, publicacion.getStock()));
                            if (cantidad > publicacion.getStock())
                                Toast.makeText(getContext(), "Stock insuficiente ("+publicacion.getStock()+")", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Error, cantidad demasiado alta", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // Realizar compra de la cantidad ingresada (si hay error de stock o fondos lo maneja el ViewModel)
                btComprar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int cantidad = Integer.parseInt(etCantidad.getText().toString());
                        btPublicacionComprar.setEnabled(false);
                        btPublicacionComprar.setText("Cargando...");
                        viewModel.comprobarFondos(publicacion, cantidad);
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    // Se llama cuando el usuario termina de elegir la/s imagen/es de su galería
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.recibirImagenesGaleria(requestCode, resultCode, data, publicacion);
    }

    // Cambiar la imagen mostrada en el ImageView (ivPreviewImagen)
    private void setImagen(int posicion) {
        Glide.with(getContext())
                .load(ApiClient.getPath()+imagenes.get(posicion).getDireccion())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(ivPreviewImagen);
        btDestacarImagen.setEnabled(imagenes.get(posicion).getEstado() != 2);
    }
}