package com.spartano.tiendamovil.ui.publicaciones;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.number.Scale;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.spartano.tiendamovil.R;
import com.spartano.tiendamovil.model.Publicacion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static android.app.Activity.RESULT_OK;

public class PublicacionFragment extends Fragment {

    private PublicacionViewModel viewModel;
    private Publicacion publicacion;

    private Button btAgregarImagen, btImagenAnterior, btImagenSiguiente;
    private ImageSwitcher ivPreviewImagen;

    private ArrayList<Uri> uris;
    private int pos = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(PublicacionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_publicacion, container, false);

        inicializarVista(root);
        return root;
    }

    private void inicializarVista(View root){
        publicacion = (Publicacion)getArguments().getSerializable("publicacion");
        btAgregarImagen = root.findViewById(R.id.btAgregarImagen);
        btImagenAnterior = root.findViewById(R.id.btImagenAnterior);
        btImagenSiguiente = root.findViewById(R.id.btImagenSiguiente);
        ivPreviewImagen = root.findViewById(R.id.ivPreviewImagen);

        uris = new ArrayList<>();

        ViewSwitcher.ViewFactory factory = new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView iv = new ImageView(getContext());
                iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                iv.setAdjustViewBounds(true);
                iv.setMaxHeight(700);
                return iv;
            }
        };

        ivPreviewImagen.setFactory(factory);

        btAgregarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uris.size() >= 10) {
                    Toast.makeText(getContext(), "Ya hay demasiadas imágenes cargadas.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent();
                i.setType("image/*");
                //i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                i.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(i, "Imagenes"), 200);
            }
        });

        btImagenAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = pos > 0 ? pos-1 : uris.size()-1;
                ivPreviewImagen.setImageURI(uris.get(pos));
            }
        });

        btImagenSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = pos < uris.size() -1 ? pos+1 : 0;
                ivPreviewImagen.setImageURI(uris.get(pos));
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 200) {
            /*if (data.getClipData() != null) {
                int cantidad = data.getClipData().getItemCount();
                if (cantidad > 10) {
                    Toast.makeText(getContext(), "Se seleccionaron muchas imágenes. El máximo es de 10", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < cantidad; i++) {
                    uris.add(data.getClipData().getItemAt(i).getUri());
                }
            } else {*/
            //uris.add(data.getData());
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
                ((ImageView)ivPreviewImagen.getCurrentView()).setImageBitmap(bitmap);

                byte[] b = baos.toByteArray();

                Log.d("salida", "Bitmap: " + bitmap.toString());
                Log.d("salida", "Byte Array: " + Arrays.toString(b));

                viewModel.prueba2(b, publicacion);
            } catch (IOException e) {
                e.printStackTrace();
            }
                //Log.d("salida", "Llamando al viewmodel...");
                //viewModel.testImagen(uris.get(0), publicacion);
            //}
            //ivPreviewImagen.setImageURI(uris.get(0));
            //pos = 0;
        }
    }
}