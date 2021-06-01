package com.spartano.tiendamovil.ui.publicaciones;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.spartano.tiendamovil.model.Comentario;
import com.spartano.tiendamovil.model.Reseña;
import com.spartano.tiendamovil.model.Usuario;
import com.spartano.tiendamovil.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabReseñasViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<List<Reseña>> reseñasMutable;
    private MutableLiveData<Boolean> publicacionEsMia;

    public LiveData<List<Reseña>> getReseñasMutable() {
        if (reseñasMutable == null)
            reseñasMutable = new MutableLiveData<>();
        return reseñasMutable;
    }

    public LiveData<Boolean> getPublicacionEsMia(){
        if (publicacionEsMia == null)
            publicacionEsMia = new MutableLiveData<>();
        return publicacionEsMia;
    }

    public TabReseñasViewModel(@NonNull Application application) {
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

    public void leerReseñas(int publicacionId) {
        Call<List<Reseña>> resAsync = ApiClient.getRetrofit().getReseñas(ApiClient.getApi().getToken(context), publicacionId);
        resAsync.enqueue(new Callback<List<Reseña>>() {
            @Override
            public void onResponse(Call<List<Reseña>> call, Response<List<Reseña>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null && !response.body().isEmpty()) {
                        reseñasMutable.postValue(response.body());
                        return;
                    }
                }
                Log.d("salida", "Error al cargar reseñas: " + response.message());
            }

            @Override
            public void onFailure(Call<List<Reseña>> call, Throwable t) {
                Log.d("salida", "Failure al cargar reseñas: " + t.getMessage());
            }
        });
    }

    public void crearReseña(Reseña reseña) {
        Call<Void> resAsync = ApiClient.getRetrofit().createReseña(ApiClient.getApi().getToken(context), reseña);
        resAsync.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    leerReseñas(reseña.getPublicacionId());
                    return;
                }
                Log.d("salida", "Error al crear reseña: " + response.message());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("salida", "Failure al crear reseña: " + t.getMessage());
            }
        });
    }
}
