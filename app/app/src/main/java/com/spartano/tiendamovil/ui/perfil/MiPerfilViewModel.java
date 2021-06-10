package com.spartano.tiendamovil.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.spartano.tiendamovil.model.Compra;
import com.spartano.tiendamovil.model.PerfilDataResponse;
import com.spartano.tiendamovil.model.Usuario;
import com.spartano.tiendamovil.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MiPerfilViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Usuario> usuarioMutable;
    private MutableLiveData<PerfilDataResponse> datosUsuarioMutable;
    private MutableLiveData<String> errorMutable;
    private MutableLiveData<List<Compra>> ventasMutable;
    private MutableLiveData<List<Compra>> comprasMutable;
    private MutableLiveData<Boolean> listaComprasVaciaMutable;
    private MutableLiveData<Boolean> listaVentasVaciaMutable;

    public LiveData<Boolean> getListaComprasVaciaMutable(){
        if(listaComprasVaciaMutable == null)
            listaComprasVaciaMutable = new MutableLiveData<>();
        return listaComprasVaciaMutable;
    }

    public LiveData<PerfilDataResponse> getDatosUsuarioMutable(){
        if(datosUsuarioMutable == null)
            datosUsuarioMutable = new MutableLiveData<>();
        return datosUsuarioMutable;
    }

    public LiveData<Boolean> getListaVentasVaciaMutable(){
        if(listaVentasVaciaMutable == null)
            listaVentasVaciaMutable = new MutableLiveData<>();
        return listaVentasVaciaMutable;
    }

    public LiveData<Usuario> getUsuarioMutable(){
        if(usuarioMutable == null)
            usuarioMutable = new MutableLiveData<>();
        return usuarioMutable;
    }

    public LiveData<List<Compra>> getComprasMutable(){
        if (comprasMutable == null)
            comprasMutable = new MutableLiveData<>();
        return comprasMutable;
    }

    public LiveData<List<Compra>> getVentasMutable(){
        if (ventasMutable == null)
            ventasMutable = new MutableLiveData<>();
        return ventasMutable;
    }

    public LiveData<String> getErrorMutable(){
        if (errorMutable == null)
            errorMutable = new MutableLiveData<>();
        return errorMutable;
    }

    public MiPerfilViewModel(@NonNull Application app){
        super(app);
        context = app.getApplicationContext();
    }

    public void obtenerDatosUsuario() {
        Call<PerfilDataResponse> resAsync = ApiClient.getRetrofit().getDatosPerfilUsuario(ApiClient.getApi().getToken(context), -1);
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

    public void ObtenerUsuario(){
        Call<Usuario> resAsync = ApiClient.getRetrofit().getUsuario(ApiClient.getApi().getToken(context));
        resAsync.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Usuario u = response.body();
                        if(u.getDireccion() == null){
                            u.setDireccion(null);
                            u.setLocalidad(null);
                            u.setProvinicia(null);
                            u.setPais(null);
                        }
                        usuarioMutable.setValue(u);
                    }
                    else {
                        Log.d("salida", "Error al buscar el usuario");
                        errorMutable.setValue("Ocurrió un error inesperado");
                    }
                }
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.d("salida", "Error de conexion");
                errorMutable.setValue("No se pudo conectar con el servidor");
            }
        });
    }

    public void ObtenerCompras(){
        Call<List<Compra>> resAsync = ApiClient.getRetrofit().getCompras(ApiClient.getApi().getToken(context));
        resAsync.enqueue(new Callback<List<Compra>>() {
            @Override
            public void onResponse(Call<List<Compra>> call, Response<List<Compra>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if(!response.body().isEmpty()){
                            comprasMutable.setValue(response.body());
                            listaComprasVaciaMutable.setValue(false);
                        }
                        else
                            listaComprasVaciaMutable.setValue(true);
                    }
                } else {
                    errorMutable.setValue("Ocurrió un error inesperado");
                    listaComprasVaciaMutable.setValue(true);
                }
                Log.d("salida", "Error (onResponse) al obtener las compras: " + response.message());
            }

            @Override
            public void onFailure(Call<List<Compra>> call, Throwable t) {
                errorMutable.setValue("No se pudo conectar con el servidor");
                listaComprasVaciaMutable.setValue(true);
                Log.d("salida", "Error (onFailure) al obtener las compras: " + t.getMessage());
            }
        });
    }

    public void ObtenerVentas(){
        Call<List<Compra>> resAsync = ApiClient.getRetrofit().getVentas(ApiClient.getApi().getToken(context));
        resAsync.enqueue(new Callback<List<Compra>>() {
            @Override
            public void onResponse(Call<List<Compra>> call, Response<List<Compra>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if(!response.body().isEmpty()){
                            ventasMutable.setValue(response.body());
                            listaVentasVaciaMutable.setValue(false);
                        }
                        else
                            listaVentasVaciaMutable.setValue(true);
                    }
                } else {
                    errorMutable.setValue("Ocurrió un error inesperado");
                    listaVentasVaciaMutable.setValue(true);
                }
                Log.d("salida", "Error (onResponse) al obtener las ventas: " + response.message());
            }

            @Override
            public void onFailure(Call<List<Compra>> call, Throwable t) {
                errorMutable.setValue("No se pudo conectar con el servidor");
                listaVentasVaciaMutable.setValue(true);
                Log.d("salida", "Error (onFailure) al obtener las ventas: " + t.getMessage());
            }
        });
    }

}