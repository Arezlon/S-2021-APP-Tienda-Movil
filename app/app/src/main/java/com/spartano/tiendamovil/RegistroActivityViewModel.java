package com.spartano.tiendamovil;

import android.app.Application;
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

public class RegistroActivityViewModel extends AndroidViewModel {

    public MutableLiveData<String> errorValidacionMutable;
    public MutableLiveData<Boolean> registroExitosoMutable;

    public MutableLiveData<String> getErrorValidacionMutable() {
        if (errorValidacionMutable == null)
            errorValidacionMutable = new MutableLiveData<>();
        return errorValidacionMutable;
    }

    public MutableLiveData<Boolean> getRegistroExitosoMutable() {
        if (registroExitosoMutable == null)
            registroExitosoMutable = new MutableLiveData<>();
        return registroExitosoMutable;
    }

    public RegistroActivityViewModel(@NonNull Application app){
        super(app);
    }

    public void validarRegistro(Usuario u, String confirmacionClave){
        if(u.getNombre().length() > 16 || u.getNombre().length() < 3)
            errorValidacionMutable.setValue("El nombre ingresado no es válido (3 a 16 caracteres)");
        else if (u.getApellido().length() > 16 || u.getApellido().length() < 3)
            errorValidacionMutable.setValue("El apellido ingresado no es válido (3 a 16 caracteres)");
        else if(u.getTelefono().length() > 15 || u.getTelefono().length() < 9)
            errorValidacionMutable.setValue("El número de teléfono no es válido (9 a 15 dígitos)");
        else if(u.getDni().length() != 8)
            errorValidacionMutable.setValue("El DNI ingresado no es válido");
        else if(!Patterns.EMAIL_ADDRESS.matcher(u.getEmail()).matches())
            errorValidacionMutable.setValue("El correo electrónico no es válido");
        else if (u.getClave().length() > 30 || u.getClave().length() < 3)
            errorValidacionMutable.setValue("La contraseña ingresada no es válida (3 a 30 caracteres)");
        else if (!u.getClave().equals(confirmacionClave))
            errorValidacionMutable.setValue("Las contraseñas ingresadas no coinciden");
        else if (u.getNombre().isEmpty() || u.getApellido().isEmpty() || u.getTelefono().isEmpty() || u.getDni().isEmpty() || u.getEmail().isEmpty() || u.getClave().isEmpty() || confirmacionClave.isEmpty())
            errorValidacionMutable.setValue("Los campos no pueden estar vacíos");
        else{
            Call<Boolean> resAsync = ApiClient.getRetrofit().createUsuario(u);
            resAsync.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()){
                        if (response.body()){
                            registroExitosoMutable.setValue(true);
                            return;
                        }
                    }
                    errorValidacionMutable.setValue("Ocurrió un error inesperado");
                    // como distinguir que tipo de error es?? entra acá cuando falla una unique constraint
                    Log.d("salida", response.message() + " " + response.code());
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    errorValidacionMutable.setValue("No se pudo conectar con el servidor");
                    Log.d("salida", t.getMessage());
                }
            });
        }
    }
}
