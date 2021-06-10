package com.spartano.tiendamovil.ui.publicaciones;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.spartano.tiendamovil.model.Compra;
import com.spartano.tiendamovil.model.Etiqueta;
import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.model.PublicacionEtiqueta;
import com.spartano.tiendamovil.model.PublicacionImagen;
import com.spartano.tiendamovil.model.Usuario;
import com.spartano.tiendamovil.request.ApiClient;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class TabPublicacionViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<String> errorMutable;
    private MutableLiveData<List<PublicacionImagen>> imagenesMutable;
    private MutableLiveData<List<PublicacionEtiqueta>> etiquetasMutable;
    private MutableLiveData<Boolean> sinImagenesMutable;
    private MutableLiveData<Boolean> publicacionEsMia;
    private MutableLiveData<Boolean> compraMutable;
    private MutableLiveData<Boolean> edicionMutable;
    private float fondos;

    public LiveData<List<PublicacionEtiqueta>> getEtiquetasMutable() {
        if (etiquetasMutable == null)
            etiquetasMutable = new MutableLiveData<>();
        return etiquetasMutable;
    }

    public LiveData<List<PublicacionImagen>> getImagenesMutable() {
        if (imagenesMutable == null)
            imagenesMutable = new MutableLiveData<>();
        return imagenesMutable;
    }

    public LiveData<String> getErrorMutable(){
        if (errorMutable == null)
            errorMutable = new MutableLiveData<>();
        return errorMutable;
    }

    public LiveData<Boolean> getSinImagenesMutable(){
        if (sinImagenesMutable == null)
            sinImagenesMutable = new MutableLiveData<>();
        return sinImagenesMutable;
    }

    public LiveData<Boolean> getPublicacionEsMia(){
        if (publicacionEsMia == null)
            publicacionEsMia = new MutableLiveData<>();
        return publicacionEsMia;
    }

    public LiveData<Boolean> getCompraMutable(){
        if (compraMutable == null)
            compraMutable = new MutableLiveData<>();
        return compraMutable;
    }

    public LiveData<Boolean> getEdicionMutable(){
        if (edicionMutable == null)
            edicionMutable = new MutableLiveData<>();
        return edicionMutable;
    }

    public TabPublicacionViewModel(@NonNull Application app) {
        super(app);
        context = app.getApplicationContext();
    }

    public  void modificarPublicacion(Publicacion publicacion, int stock, float precio, boolean estado) {
        if (stock == 0)
            errorMutable.setValue("Error, el stock mínimo es 1. Si no hay stock debe deshabilitar la publicación");
        else if (precio <= 0)
            errorMutable.setValue("Error, precio ingresado invalido");
        else {
            publicacion.setStock(stock);
            publicacion.setPrecio(precio);
            publicacion.setEstado(estado ? 1 : 0);
            Call<Void> resAsync = ApiClient.getRetrofit().editPublicacion(ApiClient.getApi().getToken(context), publicacion);
            resAsync.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        edicionMutable.setValue(true);
                    } else {
                        errorMutable.setValue("Ocurrió un error inesperado");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    errorMutable.setValue("No se pudo conectar con el servidor");
                }
            });
        }
    }

    public void crearEtiquetas(List<PublicacionEtiqueta> etiquetas, Publicacion publicacion) {
        Call<Void> resAsync = ApiClient.getRetrofit().createEtiquetas(ApiClient.getApi().getToken(context), etiquetas);
        resAsync.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    leerEtiquetas(publicacion.getId());
                }
                Log.d("salida", "Crear etiquetas: " + response.message());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMutable.setValue("Error al Crear etiquetas");
                Log.d("salida", "Error al Crear etiquetas: " + t.getMessage());
            }
        });
    }

    public void leerEtiquetas(int publicacionId) {
        Call<List<PublicacionEtiqueta>> resAsync = ApiClient.getRetrofit().getEtiquetas(ApiClient.getApi().getToken(context), publicacionId);
        resAsync.enqueue(new Callback<List<PublicacionEtiqueta>>() {
            @Override
            public void onResponse(Call<List<PublicacionEtiqueta>> call, Response<List<PublicacionEtiqueta>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null)
                        etiquetasMutable.postValue(response.body());
                    else {
                        errorMutable.postValue("No se encontraron etiquetas");
                        //sinImagenesMutable.setValue(true);
                    }
                } else {
                    errorMutable.postValue("Error al leer las etiquetas de la publicación");
                    Log.d("salida", "Error al leer etiquetas de la publicacion: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<PublicacionEtiqueta>> call, Throwable t) {
                errorMutable.postValue("No se pudo conectar con el servidor");
                Log.d("salida", "Error al leer etiquetas de la publicacion: " + t.getMessage());
            }
        });
    }

    public void deleteEtiqueta(PublicacionEtiqueta etiqueta) {
        Call<Void> resAsync = ApiClient.getRetrofit().deleteEtiqueta(ApiClient.getApi().getToken(context), etiqueta.getId());
        resAsync.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    leerEtiquetas(etiqueta.getPublicacionId());
                } else {
                    errorMutable.postValue("Error al eliminar la etiqueta");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMutable.postValue("No se pudo conectar con el servidor");
            }
        });
    }

    public void comprobarUsuario(int usuarioId) {
        Call<Usuario> resAsync = ApiClient.getRetrofit().getUsuario(ApiClient.getApi().getToken(context));
        resAsync.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    if (response.body().getId() == usuarioId)
                        publicacionEsMia.postValue(true);
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                errorMutable.setValue("Error al comprobar el dueño de la publicación");
                Log.d("salida", "Error al comprobar el dueño de la publicación: " + t.getMessage());
            }
        });
    }

    public void comprobarFondos(Publicacion publicacion, int cantidad) {
        Call<Usuario> resAsync = ApiClient.getRetrofit().getUsuario(ApiClient.getApi().getToken(context));
        resAsync.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    fondos = response.body().getFondos();
                    verificarCompra(publicacion, cantidad);
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                errorMutable.setValue("No se pudo conectar con el servidor");
                Log.d("salida", "Error: " + t.getMessage());
            }
        });
    }

    public void leerImagenesPublicacion(int id){
        Call<List<PublicacionImagen>> resAsync = ApiClient.getRetrofit().getImagenes(ApiClient.getApi().getToken(context), id);
        resAsync.enqueue(new Callback<List<PublicacionImagen>>() {
            @Override
            public void onResponse(Call<List<PublicacionImagen>> call, Response<List<PublicacionImagen>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null)
                        if (response.body().isEmpty())
                            sinImagenesMutable.setValue(true);
                        else
                            imagenesMutable.postValue(response.body());
                    else {
                        errorMutable.postValue("No se encontraron imágenes");
                        sinImagenesMutable.setValue(true);
                    }
                } else {
                    errorMutable.postValue("Error al leer las imagenes de la publicación");
                    Log.d("salida", "Error al leer imagenes de la publicacion: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<PublicacionImagen>> call, Throwable t) {
                errorMutable.postValue("No se pudo conectar con el servidor");
                Log.d("salida", "Error al leer imagenes de la publicacion: " + t.getMessage());
            }
        });
    }

    public void eliminarImagen(PublicacionImagen imagen) {
        int publicacionId = imagen.getPublicacionId();
        Call<Void> resAsync = ApiClient.getRetrofit().deleteImagen(ApiClient.getApi().getToken(context), imagen.getId());
        resAsync.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    leerImagenesPublicacion(publicacionId);
                } else {
                    errorMutable.postValue("Error al eliminar la imagen");
                    Log.d("salida", "Error al eliminar la imagen: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMutable.postValue("No se pudo conectar con el servidor");
                Log.d("salida", "Error al eliminar la imagen: " + t.getMessage());
            }
        });
    }

    public void destacarImagen(PublicacionImagen imagen) {
        int publicacionId = imagen.getPublicacionId();
        Call<Void> resAsync = ApiClient.getRetrofit().destacarImagen(ApiClient.getApi().getToken(context), imagen);
        resAsync.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    leerImagenesPublicacion(publicacionId);
                } else {
                    errorMutable.postValue("Error al destacar la imagen");
                    Log.d("salida", "Error al destacar la imagen: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMutable.postValue("No se pudo conectar con el servidor");
                Log.d("salida", "Error al destacar la imagen: " + t.getMessage());
            }
        });
    }

    public void recibirImagenesGaleria(int requestCode, int resultCode, Intent data, Publicacion publicacion) {
        if (resultCode == RESULT_OK && requestCode == 200) {
            // Agregamos todas las direcciones de las imagenes seleccionadas desde la galería a un arreglo como URIs
            ArrayList<Uri> uris = new ArrayList<>();
            if (data.getClipData() != null) {
                // Si se seleccionaron varias imagenes (se guardan en data.getClipData)
                int cantidad = data.getClipData().getItemCount();
                if (cantidad > 10) {
                    errorMutable.postValue("Se seleccionaron muchas imágenes. El máximo es de 10");
                    return;
                }

                for (int i = 0; i < cantidad; i++)
                    uris.add(data.getClipData().getItemAt(i).getUri());

            } else // Si solo se selecciono una (se guarda en data.getData)
                uris.add(data.getData());

            // Para poder subir las imagenes al servidor necesitamos que sean archivos, no direcciones URI
            // Acá las vamos escribiendo localmente para poder pasarlas al servidor (File -> IFormFile)
            ArrayList<File> files = new ArrayList<>();
            for(Uri imageUri : uris) {
                try {
                    // Convertir la imagen del URI a Bitmap, comprimirla y convertirla en byte[]
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,80, baos); // Normalizar formato y comprimir
                    byte[] b = baos.toByteArray();

                    // Escribir la imagen en un File y guardarla en el ArrayList files
                    File archivo =new File(context.getFilesDir(),"imagen_"+uris.indexOf(imageUri)+"_publicacion_"+publicacion.getId()+".jpg");
                    if(archivo.exists())
                        archivo.delete();

                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(archivo));
                    bos.write(b);
                    bos.flush();
                    bos.close();

                    files.add(archivo);
                } catch (IOException e) {
                    errorMutable.postValue("Ocurrió un error al cargar la/s imagen/es: "+e.getMessage());
                    return;
                }
            }
            // Ahora ya tenemos el arreglo de Files listo para ser subido al servidor
            subirImagenes(files, publicacion.getId());
        } else {
            errorMutable.postValue("Ocurrió un error al cargar la/s imagen/es");
        }
    }

    public void subirImagenes(ArrayList<File> files, int id){
        List<MultipartBody.Part> body = new ArrayList<>();
        for (File file : files){
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body.add(MultipartBody.Part.createFormData("imagenes", file.getName(), requestFile));
        }
        RequestBody _id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id));

        Call<Void> resAsync = ApiClient.getRetrofit().createImagenes(ApiClient.getApi().getToken(context), body, _id);
        resAsync.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful())
                    leerImagenesPublicacion(id);
                Log.d("salida", "3OnResponse " + response.message() + " " + response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("salida", "3OnFailure " + t.getMessage());
            }
        });
    }

    public void verificarCompra(Publicacion publicacion, int cantidad){

        if(cantidad <= 0)
            errorMutable.setValue("Error, la cantidad no puede ser 0");
        else if(publicacion.stock < cantidad)
            errorMutable.setValue("Error, stock insuficiente");
        else if(publicacion.precio*cantidad > fondos)
            errorMutable.setValue("Error, fondos insuficientes");
        else {
            Compra compra = new Compra();
            compra.setPublicacion(publicacion);
            compra.setCantidad(cantidad);

            Call<Void> resAsync = ApiClient.getRetrofit().createCompra(compra, ApiClient.getApi().getToken(context));
            resAsync.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        compraMutable.setValue(true);
                        return;
                    }
                    errorMutable.setValue("Ocurrió un error inesperado");
                    Log.d("salida", response.message() + " " + response.code());
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    errorMutable.setValue("No se pudo conectar con el servidor");
                    Log.d("salida", t.getMessage());
                }
            });
        }
    }
}