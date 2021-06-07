package com.spartano.tiendamovil.ui.publicaciones;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublicacionViewModel extends AndroidViewModel {
    public MutableLiveData<Publicacion> publicacionMutable;
    public MutableLiveData<String> errorMutable;
    private Context context;

    public LiveData<Publicacion> getPublicacionMutable(){
        if (publicacionMutable == null)
            publicacionMutable = new MutableLiveData<>();
        return publicacionMutable;
    }

    public LiveData<String> getErrorMutable(){
        if (errorMutable == null)
            errorMutable = new MutableLiveData<>();
        return errorMutable;
    }

    public PublicacionViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void leerPublicacion(Publicacion publicacion) {
        if (publicacion.getUsuario() != null)
            publicacionMutable.setValue(publicacion);
        else {
            Call<Publicacion> resAsync = ApiClient.getRetrofit().getPublicacion(ApiClient.getApi().getToken(context), publicacion.getId());
            resAsync.enqueue(new Callback<Publicacion>() {
                @Override
                public void onResponse(Call<Publicacion> call, Response<Publicacion> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            publicacionMutable.setValue(response.body());
                            return;
                        }
                    }
                    errorMutable.setValue("Ocurrió un error inesperado");
                    Log.d("salida", "Error al obtener la publicacion: " + response.message());
                }

                @Override
                public void onFailure(Call<Publicacion> call, Throwable t) {
                    errorMutable.setValue("No se pudo conectar con el servior");
                    Log.d("salida", "Failure al obtener la publicacion: " + t.getMessage());
                }
            });
        }
    }
}
