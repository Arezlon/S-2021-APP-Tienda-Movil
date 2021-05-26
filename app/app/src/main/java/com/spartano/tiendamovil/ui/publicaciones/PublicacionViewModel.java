package com.spartano.tiendamovil.ui.publicaciones;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.request.ApiClient;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublicacionViewModel  extends AndroidViewModel {
    private Context context;
    private MutableLiveData<String> errorMutable;

    public LiveData<String> getErrorMutable(){
        if (errorMutable == null)
            errorMutable = new MutableLiveData<>();
        return errorMutable;
    }

    public PublicacionViewModel(@NonNull Application app) {
        super(app);
        context = app.getApplicationContext();
    }

    public void testImagen(Uri uri, Publicacion publicacion){
        Log.d("salida", "Llegó al viewmodel!");
        publicacion.setDescripcion(uri.toString());
        Call<Void> resAsync = ApiClient.getRetrofit().editPublicacion(publicacion, ApiClient.getApi().getToken(context));
        resAsync.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("salida", "OnResponse " + response.message() + " " + response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("salida", "OnFailure " + t.getMessage());
            }
        });
    }

    public void prueba2(byte[] b, Publicacion publicacion){
        Log.d("salida", "Llegó al viewmodel!");
        publicacion.setDescripcion(Arrays.toString(b));
        Call<Void> resAsync = ApiClient.getRetrofit().editPublicacion(publicacion, ApiClient.getApi().getToken(context));
        resAsync.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("salida", "OnResponse " + response.message() + " " + response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("salida", "OnFailure " + t.getMessage());
            }
        });
    }
}