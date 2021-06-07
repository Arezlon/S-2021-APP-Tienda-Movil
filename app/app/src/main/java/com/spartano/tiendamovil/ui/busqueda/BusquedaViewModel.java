package com.spartano.tiendamovil.ui.busqueda;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.request.ApiClient;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusquedaViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<List<Publicacion>> publicacionesMutable;
    private MutableLiveData<Map<Integer, String>> categoriasMutable;
    private MutableLiveData<Map<Integer, String>> tiposMutable;
    private MutableLiveData<String> errorMutable;
    private MutableLiveData<String> busquedaInicialMutable;
    private MutableLiveData<Boolean> listaPublicacionesVaciaMutable;

    public BusquedaViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<Boolean> getListaPublicacionesVaciaMutable() {
        if (listaPublicacionesVaciaMutable == null)
            listaPublicacionesVaciaMutable = new MutableLiveData<>();
        return listaPublicacionesVaciaMutable;
    }

    public LiveData<List<Publicacion>> getPublicacionesMutable() {
        if (publicacionesMutable == null)
            publicacionesMutable = new MutableLiveData<>();
        return publicacionesMutable;
    }

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

    public LiveData<String> getBusquedaInicialMutable(){
        if (busquedaInicialMutable == null)
            busquedaInicialMutable = new MutableLiveData<>();
        return busquedaInicialMutable;
    }

    public void busquedaInicial(Bundle bundle) {
        if (bundle != null) {
            String busqueda = (String)bundle.getSerializable("busqueda");
            buscar(busqueda, -1, 0, 0);
            busquedaInicialMutable.setValue(busqueda);
        }
    }

    public void buscar(String texto, float precioMaximo, int categoria, int tipo) {
        Call<List<Publicacion>> resAsync = ApiClient.getRetrofit().buscarPublicaciones(ApiClient.getApi().getToken(context), texto, precioMaximo, categoria, tipo);
        resAsync.enqueue(new Callback<List<Publicacion>>() {
            @Override
            public void onResponse(Call<List<Publicacion>> call, Response<List<Publicacion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isEmpty()) {
                        publicacionesMutable.setValue(response.body());
                        listaPublicacionesVaciaMutable.setValue(false);
                    } else {
                        publicacionesMutable.setValue(response.body());
                        listaPublicacionesVaciaMutable.setValue(true);
                    }
                    return;
                }
                errorMutable.setValue("Ocurrió un error inesperado");
                listaPublicacionesVaciaMutable.setValue(true);
                Log.d("salida", response.message());
            }

            @Override
            public void onFailure(Call<List<Publicacion>> call, Throwable t) {
                Log.d("salida", t.getMessage());
                errorMutable.setValue("No se pudo conectar con el servidor");
                listaPublicacionesVaciaMutable.setValue(true);
            }
        });
    }

    public void obtenerListadosDesplegables(){
        Call<Map<Integer, String>> categoriasAync = ApiClient.getRetrofit().getCategoriasPublicaciones(ApiClient.getApi().getToken(context));
        Call<Map<Integer, String>> tiposAync = ApiClient.getRetrofit().getTiposPublicaciones(ApiClient.getApi().getToken(context));

        categoriasAync.enqueue(new Callback<Map<Integer, String>>() {
            @Override
            public void onResponse(Call<Map<Integer, String>> call, Response<Map<Integer, String>> response) {
                if (response.isSuccessful()){
                    Map<Integer, String> categorias = new LinkedHashMap<Integer, String>();
                    categorias.put(-1, "Cualquiera");
                    categorias.putAll(response.body());
                    categoriasMutable.setValue(categorias);
                }
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
                if (response.isSuccessful()) {
                    Map<Integer, String> tipos = new LinkedHashMap<Integer, String>();
                    tipos.put(-1, "Cualquiera");
                    tipos.putAll(response.body());
                    tiposMutable.setValue(tipos);
                }
                else
                    errorMutable.setValue("Ocurrió un error inesperado");
            }

            @Override
            public void onFailure(Call<Map<Integer, String>> call, Throwable t) {
                errorMutable.setValue("No se pudo conectar con el servidor");
            }
        });
    }
}