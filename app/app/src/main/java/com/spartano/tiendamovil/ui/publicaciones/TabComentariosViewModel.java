package com.spartano.tiendamovil.ui.publicaciones;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.spartano.tiendamovil.model.Comentario;
import com.spartano.tiendamovil.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabComentariosViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<List<Comentario>> comentariosMutable;

    public LiveData<List<Comentario>> getComentariosMutable() {
        if (comentariosMutable == null)
            comentariosMutable = new MutableLiveData<>();
        return comentariosMutable;
    }

    public TabComentariosViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void leerComentarios(int publicacionId) {
        Call<List<Comentario>> resAsync = ApiClient.getRetrofit().getComentarios(ApiClient.getApi().getToken(context), publicacionId);
        resAsync.enqueue(new Callback<List<Comentario>>() {
            @Override
            public void onResponse(Call<List<Comentario>> call, Response<List<Comentario>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null && !response.body().isEmpty()) {
                        comentariosMutable.postValue(response.body());
                        return;
                    }
                }
                Log.d("salida", "Error al cargar comentarios: " + response.message());
            }

            @Override
            public void onFailure(Call<List<Comentario>> call, Throwable t) {
                Log.d("salida", "Failure al cargar comentarios: " + t.getMessage());
            }
        });
    }

    public void crearComentario(Comentario comentario) {
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
    }
}
