package com.spartano.tiendamovil.ui.nuevaPublicacion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NuevaPublicacionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NuevaPublicacionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Placeholder fragment de Creación de Publicación");
    }

    public LiveData<String> getText() {
        return mText;
    }
}