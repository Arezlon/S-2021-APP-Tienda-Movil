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

    public void verificarEdicion(Usuario usuarioEditado, Usuario usuarioAnterior){

        boolean eliminarUbicacion = false;
        if(usuarioEditado.getDireccion().equals(""))
            usuarioEditado.setDireccion(null);
        if(usuarioEditado.getLocalidad().equals(""))
            usuarioEditado.setLocalidad(null);
        if(usuarioEditado.getProvinicia().equals(""))
            usuarioEditado.setProvinicia(null);
        if(usuarioEditado.getPais().equals(""))
            usuarioEditado.setPais(null);

        //Ubicación, Caso 1: Los 4 campos están vacíos -> vaciar ubicacion en bd
        if(usuarioEditado.getDireccion() == null && usuarioEditado.getLocalidad() == null && usuarioEditado.getProvinicia() == null && usuarioEditado.getPais() == null){
            eliminarUbicacion = true;
        }

        if(usuarioEditado.getNombre().length() > 16 || usuarioEditado.getNombre().length() < 3)
            errorVerificacionMutable.setValue("El nombre ingresado no es válido (3 a 16 caracteres)");
        else if (usuarioEditado.getApellido().length() > 16 || usuarioEditado.getApellido().length() < 3)
            errorVerificacionMutable.setValue("El apellido ingresado no es válido (3 a 16 caracteres)");
        else if(usuarioEditado.getTelefono().length() > 15 || usuarioEditado.getTelefono().length() < 9)
            errorVerificacionMutable.setValue("El número de teléfono no es válido (9 a 15 dígitos)");
        else if(usuarioEditado.getDni().length() != 8)
            errorVerificacionMutable.setValue("El DNI ingresado no es válido");
        else if(!Patterns.EMAIL_ADDRESS.matcher(usuarioEditado.getEmail()).matches())
            errorVerificacionMutable.setValue("El correo electrónico no es válido");
        //Ubicación, Caso 2: Algún campo está vacio y ya existía una ubicación en bd -> advertencia
        else if(!eliminarUbicacion && usuarioAnterior.getDireccion() != null && (usuarioEditado.getDireccion() == null || usuarioEditado.getLocalidad() == null || usuarioEditado.getProvinicia() == null || usuarioEditado.getPais() == null)){
            errorVerificacionMutable.setValue("Todos los campos de ubicación deben estar completos o vacíos");
        }
        //Ubicación, Caso 3: Algun/os campos están vacíos y otros no -> advertencia
        else if(usuarioEditado.getDireccion() != null && (usuarioEditado.getLocalidad() == null || usuarioEditado.getProvinicia() == null || usuarioEditado.getPais() == null)){
            errorVerificacionMutable.setValue("Todos los campos de ubicación son obligatorios si se edita la dirección");
        }
        else if(usuarioEditado.getLocalidad() != null && (usuarioEditado.getDireccion() == null || usuarioEditado.getProvinicia() == null || usuarioEditado.getPais() == null)){
            errorVerificacionMutable.setValue("Todos los campos de ubicación son obligatorios si se edita la localidad");
        }
        else if(usuarioEditado.getProvinicia() != null && (usuarioEditado.getLocalidad() == null || usuarioEditado.getDireccion() == null || usuarioEditado.getPais() == null)){
            errorVerificacionMutable.setValue("Todos los campos de ubicación son obligatorios si se edita la provinicia");
        }
        else if(usuarioEditado.getPais() != null && (usuarioEditado.getLocalidad() == null || usuarioEditado.getProvinicia() == null || usuarioEditado.getDireccion() == null)){
            errorVerificacionMutable.setValue("Todos los campos de ubicación son obligatorios si se edita el pais");
        }
        else if(usuarioEditado.getDireccion() != null && usuarioEditado.getLocalidad() != null && usuarioEditado.getProvinicia() != null && usuarioEditado.getPais() != null) {
            if(usuarioEditado.getDireccion().length() < 4 || usuarioEditado.getDireccion().length() > 50 || usuarioEditado.getLocalidad().length() < 4 || usuarioEditado.getLocalidad().length() > 50 || usuarioEditado.getProvinicia().length() < 4 || usuarioEditado.getProvinicia().length() > 50 || usuarioEditado.getPais().length() < 4 || usuarioEditado.getPais().length() > 50)
                errorVerificacionMutable.setValue("Los datos de la ubicación no son válidos (4 a 50 caracteres)");
        }
        else{
            Call<Void> resAsync = ApiClient.getRetrofit().editUsuario(usuarioEditado, ApiClient.getApi().getToken(context));
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