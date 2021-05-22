package com.spartano.tiendamovil.ui.busqueda;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BusquedaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BusquedaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Placeholder fragment de Búsqueda Avanzada");
    }

    public LiveData<String> getText() {
        return mText;
    }
}