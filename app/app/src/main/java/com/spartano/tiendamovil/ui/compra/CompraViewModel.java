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
import com.spartano.tiendamovil.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompraViewModel extends AndroidViewModel {
    public Context context;
    private MutableLiveData<Compra> compraMutable;
    public MutableLiveData<String> errorMutable;

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

}