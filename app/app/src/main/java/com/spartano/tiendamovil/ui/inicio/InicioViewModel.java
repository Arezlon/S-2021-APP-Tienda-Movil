package com.spartano.tiendamovil.ui.inicio;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.model.Usuario;
import com.spartano.tiendamovil.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Usuario> usuarioMutable;
    public MutableLiveData<List<Publicacion>> publicacionesDestacadasMutable;
    public MutableLiveData<String> errorMutable;

    public LiveData<Usuario> getUsuarioMutable(){
        if(usuarioMutable == null)
            usuarioMutable = new MutableLiveData<>();
        return usuarioMutable;
    }

    public LiveData<List<Publicacion>> getPublicacionesDestacadasMutable() {
        if (publicacionesDestacadasMutable == null)
            publicacionesDestacadasMutable = new MutableLiveData<>();
        return publicacionesDestacadasMutable;
    }

    public LiveData<String> getErrorMutable() {
        if (errorMutable == null)
            errorMutable = new MutableLiveData<>();
        return errorMutable;
    }

    public InicioViewModel(@NonNull Application app) {
        super(app);
        context = app.getApplicationContext();
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

    public void leerPublicacionesDestacadas() {
        Call<List<Publicacion>> resAsync = ApiClient.getRetrofit().getPublicacionesDestacadas(ApiClient.getApi().getToken(context));
        resAsync.enqueue(new Callback<List<Publicacion>>() {
            @Override
            public void onResponse(Call<List<Publicacion>> call, Response<List<Publicacion>> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isEmpty()) {
                        publicacionesDestacadasMutable.postValue(response.body());
                    } else {
                        errorMutable.postValue("No se encontraron publicaciones destacadas");
                    }
                } else {
                    errorMutable.postValue("Error al obtener las publicaciones destacadas");
                    Log.d("salida", "Error al obtener las publicaciones destacadas: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Publicacion>> call, Throwable t) {
                errorMutable.postValue("No se pudo conectar con el servidor");
                Log.d("salida", "Error al obtener las publicaciones destacadas: " + t.getMessage());
            }
        });
    }
}