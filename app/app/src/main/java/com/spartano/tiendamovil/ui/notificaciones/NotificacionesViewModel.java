package com.spartano.tiendamovil.ui.notificaciones;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.spartano.tiendamovil.model.Notificacion;
import com.spartano.tiendamovil.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificacionesViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<List<Notificacion>> notificacionesMutable;
    private MutableLiveData<String> errorMutable;

    public NotificacionesViewModel(@NonNull Application app){
        super(app);
        context = app.getApplicationContext();
    }

    public LiveData<List<Notificacion>> getNotificacionesMutable(){
        if (notificacionesMutable == null)
            notificacionesMutable = new MutableLiveData<>();
        return notificacionesMutable;
    }

    public LiveData<String> getErrorMutable(){
        if (errorMutable == null)
            errorMutable = new MutableLiveData<>();
        return errorMutable;
    }

    public void ObtenerNotificaciones(){
        Call<List<Notificacion>> resAsync = ApiClient.getRetrofit().getNotificaciones(ApiClient.getApi().getToken(context));
        resAsync.enqueue(new Callback<List<Notificacion>>() {
            @Override
            public void onResponse(Call<List<Notificacion>> call, Response<List<Notificacion>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        notificacionesMutable.setValue(response.body());
                    }
                } else {
                    errorMutable.setValue("Ocurrió un error inesperado");
                }
            }

            @Override
            public void onFailure(Call<List<Notificacion>> call, Throwable t) {
                errorMutable.setValue("No se pudo conectar con el servidor");
                Log.d("salida", "Error al obtener las notificaciones: " + t.getMessage());
            }
        });
    }

    public void LeerNotificacion(Notificacion notificacion){
        Call<Void> resAsync = ApiClient.getRetrofit().leerNotificacion(ApiClient.getApi().getToken(context), notificacion);
        resAsync.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    ObtenerNotificaciones();
                } else {
                    errorMutable.postValue("Error al leer la notificacion");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMutable.postValue("No se pudo conectar con el servidor");
            }
        });
    }

}