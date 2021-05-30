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

    public void verificarEdicion(Usuario usuarioEditado) {

        // Validacion de los campos que no son de la ubicación
        boolean datosValidos = false;
        if (usuarioEditado.getNombre().length() > 16 || usuarioEditado.getNombre().length() < 3)
            errorVerificacionMutable.setValue("El nombre ingresado no es válido (3 a 16 caracteres)");
        else if (usuarioEditado.getApellido().length() > 16 || usuarioEditado.getApellido().length() < 3)
            errorVerificacionMutable.setValue("El apellido ingresado no es válido (3 a 16 caracteres)");
        else if (usuarioEditado.getTelefono().length() > 15 || usuarioEditado.getTelefono().length() < 9)
            errorVerificacionMutable.setValue("El número de teléfono no es válido (9 a 15 dígitos)");
        else if (usuarioEditado.getDni().length() != 8)
            errorVerificacionMutable.setValue("El DNI ingresado no es válido");
        else if (!Patterns.EMAIL_ADDRESS.matcher(usuarioEditado.getEmail()).matches())
            errorVerificacionMutable.setValue("El correo electrónico no es válido");
        else
            datosValidos = true;

        // Validacion de campos de ubicación
        boolean edicionVacia = usuarioEditado.getDireccion().equals("") && usuarioEditado.getLocalidad().equals("") && usuarioEditado.getProvinicia().equals("") && usuarioEditado.getPais().equals("");
        boolean edicionLlena = !usuarioEditado.getDireccion().equals("") && !usuarioEditado.getLocalidad().equals("") && !usuarioEditado.getProvinicia().equals("") && !usuarioEditado.getPais().equals("");
        boolean ubicacionValidada = false;

        // Solo hay dos caminos para que direccionValida sea true, si todos los campos de direccion estan vacios o si todos estan llenos y con longitudes válidas
        if (edicionVacia) {
            // Si todos los campos de ubicacion estan vacias quiere decir que el usuario no tenía dirección o que quiere borrarla
            // Sea cual sea el caso, settear los valores de bd a null
            usuarioEditado.setDireccion(null);
            usuarioEditado.setLocalidad(null);
            usuarioEditado.setProvinicia(null);
            usuarioEditado.setPais(null);
            ubicacionValidada = true;
        } else if (edicionLlena) {
            // Si todos los campos de ubicacion estan llenos quiere decir que el usuario quiere settear o editar su ubicacion
            // Validar longitudes
            if(usuarioEditado.getDireccion().length() < 4 || usuarioEditado.getDireccion().length() > 50)
                errorVerificacionMutable.setValue("Los datos de la dirección no son válidos (4 a 50 caracteres)");
            else if(usuarioEditado.getLocalidad().length() < 4 || usuarioEditado.getLocalidad().length() > 50)
                errorVerificacionMutable.setValue("Los datos de la localidad no son válidos (4 a 50 caracteres)");
            else if(usuarioEditado.getProvinicia().length() < 4 || usuarioEditado.getProvinicia().length() > 50)
                errorVerificacionMutable.setValue("Los datos de la provincia no son válidos (4 a 50 caracteres)");
            else if(usuarioEditado.getPais().length() < 4 || usuarioEditado.getPais().length() > 50)
                errorVerificacionMutable.setValue("Los datos del pais no son válidos (4 a 50 caracteres)");
            else
                ubicacionValidada = true;
        } else {
            // Si no estan todos vacios o todos llenos la direción esta incompleta
            errorVerificacionMutable.setValue("Los datos de la ubicación no son válidos (deben estar todos llenos o vaciós)");
        }

        if (ubicacionValidada && datosValidos) {
            Call<Void> resAsync = ApiClient.getRetrofit().editUsuario(usuarioEditado, ApiClient.getApi().getToken(context));
            resAsync.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
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