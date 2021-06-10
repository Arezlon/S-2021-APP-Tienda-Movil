package com.spartano.tiendamovil.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.spartano.tiendamovil.model.PerfilDataResponse;
import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.model.Usuario;
import com.spartano.tiendamovil.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<List<Publicacion>> publicacionesMutable;
    private MutableLiveData<String> errorMutable;
    private MutableLiveData<PerfilDataResponse> datosUsuarioMutable;
    private MutableLiveData<Boolean> listaPublicacionesVaciaMutable;

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<List<Publicacion>> getPublicacionesMutable() {
        if (publicacionesMutable == null)
            publicacionesMutable = new MutableLiveData<>();
        return publicacionesMutable;
    }

    public LiveData<String> getErrorMutable(){
        if (errorMutable == null)
            errorMutable = new MutableLiveData<>();
        return errorMutable;
    }

    public LiveData<PerfilDataResponse> getDatosUsuarioMutable(){
        if(datosUsuarioMutable == null)
            datosUsuarioMutable = new MutableLiveData<>();
        return datosUsuarioMutable;
    }

    public LiveData<Boolean> getListaPublicacionesVaciaMutable(){
        if (listaPublicacionesVaciaMutable == null)
            listaPublicacionesVaciaMutable = new MutableLiveData<>();
        return listaPublicacionesVaciaMutable;
    }

    public void obtenerDatosUsuario(int id) {
        Call<PerfilDataResponse> resAsync = ApiClient.getRetrofit().getDatosPerfilUsuario(ApiClient.getApi().getToken(context), id);
        resAsync.enqueue(new Callback<PerfilDataResponse>() {
            @Override
            public void onResponse(Call<PerfilDataResponse> call, Response<PerfilDataResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Usuario u = response.body().getUsuario();
                        if(u.getDireccion() == null){
                            u.setDireccion(null);
                            u.setLocalidad(null);
                            u.setProvinicia(null);
                            u.setPais(null);
                        }
                        datosUsuarioMutable.setValue(response.body());
                    }
                    else {
                        Log.d("salida", "Error al buscar el usuario");
                        errorMutable.setValue("Ocurrió un error inesperado");
                    }
                }
            }

            @Override
            public void onFailure(Call<PerfilDataResponse> call, Throwable t) {
                Log.d("salida", "Error de conexion");
                errorMutable.setValue("No se pudo conectar con el servidor");
            }
        });
    }

    public void leerPublicacionesUsuario(Usuario usuario) {
        Call<List<Publicacion>> resAsync = ApiClient.getRetrofit().getPublicacionesUsuario(ApiClient.getApi().getToken(context), usuario.getId());
        resAsync.enqueue(new Callback<List<Publicacion>>() {
            @Override
            public void onResponse(Call<List<Publicacion>> call, Response<List<Publicacion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isEmpty()) {
                        publicacionesMutable.setValue(response.body());
                        listaPublicacionesVaciaMutable.setValue(false);
                    } else {
                        listaPublicacionesVaciaMutable.setValue(true);
                    }
                    return;
                }
                Log.d("salida", "Error al obtener lista de publicaciones del vendedor: " + response.message());
                errorMutable.setValue("Ocurrió un error inesperado");
                listaPublicacionesVaciaMutable.setValue(true);
            }

            @Override
            public void onFailure(Call<List<Publicacion>> call, Throwable t) {
                Log.d("salida", "Error al obtener lista de publicaciones del vendedor: " + t.getMessage());
                errorMutable.setValue("No se pudo conectar con el servidor");
                listaPublicacionesVaciaMutable.setValue(true);
            }
        });
    }
}
