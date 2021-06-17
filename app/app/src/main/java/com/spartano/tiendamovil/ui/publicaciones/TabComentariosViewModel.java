package com.spartano.tiendamovil.ui.publicaciones;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.spartano.tiendamovil.model.Comentario;
import com.spartano.tiendamovil.model.Usuario;
import com.spartano.tiendamovil.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabComentariosViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<List<Comentario>> comentariosMutable;
    private MutableLiveData<Boolean> publicacionEsMia;
    private MutableLiveData<Boolean> listaVaciaMutable;
    private MutableLiveData<String> errorMutable;

    public LiveData<Boolean> getListaVaciaMutable(){
        if(listaVaciaMutable == null)
            listaVaciaMutable = new MutableLiveData<>();
        return listaVaciaMutable;
    }

    public LiveData<String> getErrorMutable(){
        if (errorMutable == null)
            errorMutable = new MutableLiveData<>();
        return errorMutable;
    }

    public LiveData<List<Comentario>> getComentariosMutable() {
        if (comentariosMutable == null)
            comentariosMutable = new MutableLiveData<>();
        return comentariosMutable;
    }

    public LiveData<Boolean> getPublicacionEsMia(){
        if (publicacionEsMia == null)
            publicacionEsMia = new MutableLiveData<>();
        return publicacionEsMia;
    }

    public TabComentariosViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void comprobarUsuario(int usuarioId) {
        Call<Usuario> resAsync = ApiClient.getRetrofit().getUsuario(ApiClient.getApi().getToken(context));
        resAsync.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    publicacionEsMia.postValue(response.body().getId() == usuarioId);
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.d("salida", "Error al comprobar el dueño de la publicación: " + t.getMessage());
            }
        });
    }

    public void leerComentarios(int publicacionId) {
        Call<List<Comentario>> resAsync = ApiClient.getRetrofit().getComentarios(ApiClient.getApi().getToken(context), publicacionId);
        resAsync.enqueue(new Callback<List<Comentario>>() {
            @Override
            public void onResponse(Call<List<Comentario>> call, Response<List<Comentario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if(!response.body().isEmpty()) {
                        comentariosMutable.postValue(response.body());
                        listaVaciaMutable.setValue(false);
                    }else{
                        listaVaciaMutable.setValue(true);
                    }
                } else {
                    errorMutable.setValue("Ocurrió un error inesperado");
                    listaVaciaMutable.setValue(true);
                    Log.d("salida", "Error al cargar comentarios: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Comentario>> call, Throwable t) {
                errorMutable.setValue("No se pudo conectar con el servidor");
                Log.d("salida", "Failure al cargar comentarios: " + t.getMessage());
                listaVaciaMutable.setValue(true);
            }
        });
    }

    public void crearComentario(Comentario comentario) {
        if (comentario.getPregunta() != null && !comentario.getPregunta().equals("")) {
            Call<Void> resAsync = ApiClient.getRetrofit().createComentario(ApiClient.getApi().getToken(context), comentario);
            resAsync.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        leerComentarios(comentario.getPublicacionId());
                        return;
                    }
                    Log.d("salida", "Error al crear comentario: " + response.message());
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.d("salida", "Failure al crear comentario: " + t.getMessage());
                }
            });
        } else {
            errorMutable.setValue("Error");
            leerComentarios(comentario.getPublicacionId());
        }
    }

    public void responderComentario(Comentario comentario, String respuesta) {
        if (respuesta != null && !respuesta.equals("")) {
            comentario.setRespuesta(respuesta);
            Call<Void> resAsync = ApiClient.getRetrofit().responderComentario(ApiClient.getApi().getToken(context), comentario);
            resAsync.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        leerComentarios(comentario.getPublicacionId());
                        return;
                    }
                    Log.d("salida", "Error al responder comentario: " + response.message());
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.d("salida", "Failure al responder comentario: " + t.getMessage());
                }
            });
        } else {
            errorMutable.setValue("Error");
            leerComentarios(comentario.getPublicacionId());
        }
    }
}
