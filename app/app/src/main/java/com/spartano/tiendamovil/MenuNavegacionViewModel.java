package com.spartano.tiendamovil;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.spartano.tiendamovil.model.Usuario;
import com.spartano.tiendamovil.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuNavegacionViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Usuario> usuarioMutable;
    private MutableLiveData<String> totalNotificacionesMutable;

    public MenuNavegacionViewModel(@NonNull Application app) {
        super(app);
        context = app.getApplicationContext();
    }

    public LiveData<String> getTotalNotificacionesMutable(){
        if(totalNotificacionesMutable == null)
            totalNotificacionesMutable = new MutableLiveData<>();
        return totalNotificacionesMutable;
    }

    public LiveData<Usuario> getUsuarioMutable(){
        if(usuarioMutable == null)
            usuarioMutable = new MutableLiveData<>();
        return usuarioMutable;
    }

    public void ObtenerUsuario(){
        Call<Usuario> resAsync = ApiClient.getRetrofit().getUsuario(ApiClient.getApi().getToken(context));
        resAsync.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Usuario u = response.body();
                        usuarioMutable.setValue(u);
                    }
                    else
                        Log.d("salida", "Error al buscar el usuario");
                }
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.d("salida", "Error de conexion");
            }
        });
    }

    public void ObtenerCantidadNotificaciones(){
        Call<Integer> resAsync = ApiClient.getRetrofit().getCantidadNotificaciones(ApiClient.getApi().getToken(context));
        resAsync.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if(response.body() > 0)
                            totalNotificacionesMutable.setValue(response.body().toString());
                        else if(response.body() > 99)
                            totalNotificacionesMutable.setValue("99+");
                        else
                            totalNotificacionesMutable.setValue("");
                    }
                    else
                        Log.d("salida", "Error obtener la cantidad de notificaciones");
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("salida", "Error de conexion");
            }
        });
    }
}
