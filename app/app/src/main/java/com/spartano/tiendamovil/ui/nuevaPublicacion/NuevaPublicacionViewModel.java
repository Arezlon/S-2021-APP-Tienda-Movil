package com.spartano.tiendamovil.ui.nuevaPublicacion;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.request.ApiClient;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevaPublicacionViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Map<Integer, String>> categoriasMutable;
    private MutableLiveData<Map<Integer, String>> tiposMutable;
    private MutableLiveData<String> errorMutable;
    private MutableLiveData<Boolean> exitoMutable;

    public LiveData<Map<Integer, String>> getCategoriasMutable(){
        if (categoriasMutable == null)
            categoriasMutable = new MutableLiveData<>();
        return categoriasMutable;
    }

    public LiveData<Map<Integer, String>> getTiposMutable(){
        if (tiposMutable == null)
            tiposMutable = new MutableLiveData<>();
        return tiposMutable;
    }

    public LiveData<String> getErrorMutable(){
        if (errorMutable == null)
            errorMutable = new MutableLiveData<>();
        return errorMutable;
    }

    public LiveData<Boolean> getExitoMutable(){
        if (exitoMutable == null)
            exitoMutable = new MutableLiveData<>();
        return exitoMutable;
    }

    public NuevaPublicacionViewModel(@NonNull Application app) {
        super(app);
        context = app.getApplicationContext();
    }

    public void nuevaPublicacion(Publicacion p){
        if (p.getTitulo().length() < 5)
            errorMutable.setValue("El título no puede tener menos de 5 caracteres");
        else if (p.getTitulo().length() > 100)
            errorMutable.setValue("El título no puede tener más de 100 caracteres");
        else if (p.getDescripcion().length() < 10)
            errorMutable.setValue("La descripcion no puede tener menos de 10 caracteres");
        else if (p.getDescripcion().length() > 2000)
            errorMutable.setValue("La descripcion no puede tener más de 2000 caracteres");
        else if (p.getPrecio() <= 0 || p.getPrecio() > 9999999)
            errorMutable.setValue("El precio ingresado no es válido");
        else if (p.getStock() <= 0 || p.getStock() > 9999)
            errorMutable.setValue("El stock ingresado no es válido");
        else if (p.getTitulo().isEmpty() || p.getDescripcion().isEmpty())
            errorMutable.setValue("Los campos no pueden estar vacíos");
        else {
            Call<Void> resAsync = ApiClient.getRetrofit().createPublicacion(p, ApiClient.getApi().getToken(context));
            resAsync.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful())
                        exitoMutable.setValue(true);
                    else {
                        errorMutable.setValue("Ocurrió un error inesperado");
                    }
                    Log.d("salida", "Crear publicacion/response: " + response.message());
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    errorMutable.setValue("No se pudo conectar con el servidor");
                    Log.d("salida", "Crear publicacion/failure: " + t.getMessage());
                }
            });
        }
    }

    public void obtenerListadosDesplegables(){
        Call<Map<Integer, String>> categoriasAync = ApiClient.getRetrofit().getCategoriasPublicaciones(ApiClient.getApi().getToken(context));
        Call<Map<Integer, String>> tiposAync = ApiClient.getRetrofit().getTiposPublicaciones(ApiClient.getApi().getToken(context));

        categoriasAync.enqueue(new Callback<Map<Integer, String>>() {
            @Override
            public void onResponse(Call<Map<Integer, String>> call, Response<Map<Integer, String>> response) {
                if (response.isSuccessful())
                    categoriasMutable.setValue(response.body());
                else
                    errorMutable.setValue("Ocurrió un error inesperado");
            }

            @Override
            public void onFailure(Call<Map<Integer, String>> call, Throwable t) {
                errorMutable.setValue("No se pudo conectar con el servidor");
            }
        });

        tiposAync.enqueue(new Callback<Map<Integer, String>>() {
            @Override
            public void onResponse(Call<Map<Integer, String>> call, Response<Map<Integer, String>> response) {
                if (response.isSuccessful())
                    tiposMutable.setValue(response.body());
                else
                    errorMutable.setValue("Ocurrió un error inesperado");
            }

            @Override
            public void onFailure(Call<Map<Integer, String>> call, Throwable t) {
                errorMutable.setValue("No se pudo conectar con el servidor");
            }
        });
    }

    /*public LiveData<String> getText() {
        return mText;
    }*/
}