package com.spartano.tiendamovil.ui.publicaciones;

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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublicacionesViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<String> errorMutable;
    private MutableLiveData<List<Publicacion>> publicacionesMutable;
    private MutableLiveData<Boolean> listaPublicacionesVaciaMutable;

    public LiveData<Boolean> getListaPublicacionesVaciaMutable(){
        if(listaPublicacionesVaciaMutable == null)
            listaPublicacionesVaciaMutable = new MutableLiveData<>();
        return listaPublicacionesVaciaMutable;
    }

    public LiveData<List<Publicacion>> getPublicacionesMutable(){
        if (publicacionesMutable == null)
            publicacionesMutable = new MutableLiveData<>();
        return publicacionesMutable;
    }

    public LiveData<String> getErrorMutable(){
        if (errorMutable == null)
            errorMutable = new MutableLiveData<>();
        return errorMutable;
    }

    public PublicacionesViewModel(@NonNull Application app){
        super(app);
        context = app.getApplicationContext();
    }

    public void leerMisPublicaciones(){
        Call<List<Publicacion>> resAsync = ApiClient.getRetrofit().getMisPublicaciones(ApiClient.getApi().getToken(context));
        resAsync.enqueue(new Callback<List<Publicacion>>() {
            @Override
            public void onResponse(Call<List<Publicacion>> call, Response<List<Publicacion>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if(!response.body().isEmpty()) {
                            publicacionesMutable.setValue(response.body());
                            listaPublicacionesVaciaMutable.setValue(false);
                        }else{
                            listaPublicacionesVaciaMutable.setValue(true);
                        }
                    }
                } else {
                    errorMutable.setValue("Ocurrió un error inesperado");
                    listaPublicacionesVaciaMutable.setValue(true);
                }
                Log.d("salida", "Leer mis publicaciones/response: " + response.message());
            }

            @Override
            public void onFailure(Call<List<Publicacion>> call, Throwable t) {
                errorMutable.setValue("No se pudo conectar con el servidor");
                listaPublicacionesVaciaMutable.setValue(true);
                Log.d("salida", "Leer mis publicaciones/failure: " + t.getMessage());
            }
        });
    }
}