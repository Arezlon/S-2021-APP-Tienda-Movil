package com.spartano.tiendamovil.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.spartano.tiendamovil.model.Usuario;
import com.spartano.tiendamovil.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilViewModel extends AndroidViewModel {

    public Context context;
    public MutableLiveData<String> errorVerificacionMutable;
    public MutableLiveData<Boolean> edicionCorrectaMutable;

    public EditarPerfilViewModel(@NonNull Application app){
        super(app);
        context = app.getApplicationContext();
    }

    public MutableLiveData<String> getErrorVerificacionMutable() {
        if (errorVerificacionMutable == null)
            errorVerificacionMutable = new MutableLiveData<>();
        return errorVerificacionMutable;
    }

    public MutableLiveData<Boolean> getEdicionCorrectaMutable() {
        if (edicionCorrectaMutable == null)
            edicionCorrectaMutable = new MutableLiveData<>();
        return edicionCorrectaMutable;
    }

    public void verificarEdicion(Usuario u){
        if(u.getNombre().length() > 16 || u.getNombre().length() < 3)
            errorVerificacionMutable.setValue("El nombre ingresado no es válido (3 a 16 caracteres)");
        else if (u.getApellido().length() > 16 || u.getApellido().length() < 3)
            errorVerificacionMutable.setValue("El apellido ingresado no es válido (3 a 16 caracteres)");
        else if(u.getTelefono().length() > 15 || u.getTelefono().length() < 9)
            errorVerificacionMutable.setValue("El número de teléfono no es válido (9 a 15 dígitos)");
        else if(u.getDni().length() != 8)
            errorVerificacionMutable.setValue("El DNI ingresado no es válido");
        else if(!Patterns.EMAIL_ADDRESS.matcher(u.getEmail()).matches())
            errorVerificacionMutable.setValue("El correo electrónico no es válido");
        else{
            Call<Void> resAsync = ApiClient.getRetrofit().editUsuario(u, ApiClient.getApi().getToken(context));
            resAsync.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()){
                        edicionCorrectaMutable.setValue(true);
                        return;
                    }
                    errorVerificacionMutable.setValue("Ocurrió un error inesperado");
                    Log.d("salida", response.message() + " " + response.code());
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    errorVerificacionMutable.setValue("No se pudo conectar con el servidor");
                    Log.d("salida", t.getMessage());
                }
            });
        }
    }
}