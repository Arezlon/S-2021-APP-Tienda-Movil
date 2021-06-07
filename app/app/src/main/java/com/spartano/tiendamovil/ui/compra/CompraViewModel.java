package com.spartano.tiendamovil.ui.compra;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.spartano.tiendamovil.model.Compra;
import com.spartano.tiendamovil.model.Reseña;
import com.spartano.tiendamovil.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompraViewModel extends AndroidViewModel {
    public Context context;
    private MutableLiveData<Compra> compraMutable;
    public MutableLiveData<String> errorMutable;
    public MutableLiveData<Boolean> pemitirReseñaMutable;

    public CompraViewModel(@NonNull Application app){
        super(app);
        context = app.getApplicationContext();
    }

    public LiveData<Compra> getCompraMutable(){
        if(compraMutable == null)
            compraMutable = new MutableLiveData<>();
        return compraMutable;
    }

    public LiveData<String> getErrorMutable(){
        if (errorMutable == null)
            errorMutable = new MutableLiveData<>();
        return errorMutable;
    }

    public LiveData<Boolean> getPermitirReseñaMutable(){
        if (pemitirReseñaMutable == null)
            pemitirReseñaMutable = new MutableLiveData<>();
        return pemitirReseñaMutable;
    }

    public void ObtenerCompra(Bundle bundle){
        if(bundle == null || bundle.isEmpty()) {
            Call<Compra> resAsync = ApiClient.getRetrofit().getUltima(ApiClient.getApi().getToken(context));
            resAsync.enqueue(new Callback<Compra>() {
                @Override
                public void onResponse(Call<Compra> call, Response<Compra> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            Compra c = response.body();
                            compraMutable.setValue(c);
                        } else
                            Log.d("salida", "Error al buscar los detalles de la compra");
                    }
                }

                @Override
                public void onFailure(Call<Compra> call, Throwable t) {
                    Log.d("salida", "Error de conexion");
                }
            });
        }
        else
            compraMutable.setValue((Compra)bundle.getSerializable("compra"));
    }

    public void ComprobarReseña(int publicacionId, int compraId){
        Call<Boolean> resAsync = ApiClient.getRetrofit().comprobarReseña(ApiClient.getApi().getToken(context), publicacionId);
        resAsync.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        comprobarUsuarioReseña(compraId);
                    } else
                        Log.d("salida", "Error");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("salida", "Error de conexion");
            }
        });
    }

    public void comprobarUsuarioReseña(int compraId){
        Call<Boolean> resAsync = ApiClient.getRetrofit().comprobarUsuario(ApiClient.getApi().getToken(context), compraId);
        resAsync.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        pemitirReseñaMutable.setValue(true);
                    } else
                        Log.d("salida", "Error");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("salida", "Error de conexion");
            }
        });

    }

    public void ValidarReseña(Reseña reseña){
        if(reseña.getPuntaje() > 5 || reseña.getPuntaje() < 0){
            errorMutable.setValue("Error, puntaje inválido (0-5)");
        }else if(reseña.getEncabezado().length() < 6 || reseña.getEncabezado().length() > 24){
            errorMutable.setValue("Error, título inválido (6 a 24 caracteres)");
        }
        else if(reseña.getContenido().length() < 20 || reseña.getContenido().length() > 140){
            errorMutable.setValue("Error, descripción inválida (20 a 140 caracteres)");
        }
        else{
            Call<Void> resAsync = ApiClient.getRetrofit().createReseña(ApiClient.getApi().getToken(context), reseña);
            resAsync.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Bundle b = new Bundle();
                        b.putSerializable("compra", compraMutable.getValue());
                        ObtenerCompra(b);
                    }
                    else{
                        errorMutable.setValue("Ocurrió un error inesperado ?");
                        Log.d("salida", response.message() + " " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    errorMutable.setValue("No se pudo conectar con el servidor");
                    Log.d("salida", t.getMessage());
                }
            });
        }
    }

}