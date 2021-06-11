package com.spartano.tiendamovil.ui.cerrarSesion;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class CerrarSesionViewModel extends AndroidViewModel {
    private SharedPreferences preferences;
    private Context context;

    public CerrarSesionViewModel(@NonNull Application app){
        super(app);
        context = app.getApplicationContext();
        preferences = app.getApplicationContext().getSharedPreferences("data.dat", 0);
    }

    public void cerrarSesion(){
        preferences.edit().clear().apply();
    }
}