package com.spartano.tiendamovil.ui.fondos;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.spartano.tiendamovil.model.Transaccion;
import com.spartano.tiendamovil.model.Usuario;
import com.spartano.tiendamovil.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FondosViewModel extends AndroidViewModel {

    public Context context;
    public MutableLiveData<String> errorCargaMutable;
    public MutableLiveData<Boolean> cargaCorrectaMutable;
    private MutableLiveData<Usuario> usuarioMutable;
    private MutableLiveData<List<Transaccion>> transaccionesMutable;
    public MutableLiveData<String> errorMutable;
    private MutableLiveData<Boolean> listaTransaccionesVaciaMutable;

    public LiveData<Boolean> getListaTransaccionesVaciaMutable(){
        if(listaTransaccionesVaciaMutable == null)
            listaTransaccionesVaciaMutable = new MutableLiveData<>();
        return listaTransaccionesVaciaMutable;
    }

    public FondosViewModel(@NonNull Application app){
        super(app);
        context = app.getApplicationContext();
    }

    public MutableLiveData<String> getErrorCargaMutable() {
        if (errorCargaMutable == null)
            errorCargaMutable = new MutableLiveData<>();
        return errorCargaMutable;
    }

    public MutableLiveData<Boolean> getCargaCorrectaMutable() {
        if (cargaCorrectaMutable == null)
            cargaCorrectaMutable = new MutableLiveData<>();
        return cargaCorrectaMutable;
    }

    public LiveData<Usuario> getUsuarioMutable(){
        if(usuarioMutable == null)
            usuarioMutable = new MutableLiveData<>();
        return usuarioMutable;
    }

    public LiveData<List<Transaccion>> getTransaccionesMutable(){
        if (transaccionesMutable == null)
            transaccionesMutable = new MutableLiveData<>();
        return transaccionesMutable;
    }

    public LiveData<String> getErrorMutable(){
        if (errorMutable == null)
            errorMutable = new MutableLiveData<>();
        return errorMutable;
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

    public void verificarCargaFondos(Transaccion transaccion){
        if(transaccion.importe > 1000000)
            errorCargaMutable.setValue("El monto para la carga de fondos no puede ser mayor a $1.000.000");
        else if(transaccion.importe < 100)
            errorCargaMutable.setValue("El monto para la carga de fondos no puede ser menor a $100");
        else {
            Call<Void> resAsync = ApiClient.getRetrofit().createTransaccion(transaccion, ApiClient.getApi().getToken(context));
            resAsync.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        cargaCorrectaMutable.setValue(true);
                        return;
                    }
                    errorCargaMutable.setValue("Ocurrió un error inesperado");
                    Log.d("salida", response.message() + " " + response.code());
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    errorCargaMutable.setValue("No se pudo conectar con el servidor");
                    Log.d("salida", t.getMessage());
                }
            });
        }
    }

    public void leerHistorialTransacciones(){
        Call<List<Transaccion>> resAsync = ApiClient.getRetrofit().getTransacciones(ApiClient.getApi().getToken(context));
        resAsync.enqueue(new Callback<List<Transaccion>>() {
            @Override
            public void onResponse(Call<List<Transaccion>> call, Response<List<Transaccion>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if(!response.body().isEmpty()) {
                            transaccionesMutable.setValue(response.body());
                            listaTransaccionesVaciaMutable.setValue(false);
                        }else{
                            listaTransaccionesVaciaMutable.setValue(true);
                        }
                    }
                } else {
                    errorMutable.setValue("Ocurrió un error al cargar el historial de transacciones");
                }
                Log.d("salida", "historial " + response.message());
            }

            @Override
            public void onFailure(Call<List<Transaccion>> call, Throwable t) {
                errorMutable.setValue("No se pudo conectar con el servidor");
            }
        });
    }
}