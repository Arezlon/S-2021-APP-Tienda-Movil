package com.spartano.tiendamovil.ui.publicaciones;

import android.app.Application;
import android.content.Context;
import android.database.Observable;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.request.ApiClient;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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

    public void subirImagenes(ArrayList<File> files, int id){
        List<MultipartBody.Part> body = new ArrayList<>();
        for (File file : files){
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body.add(MultipartBody.Part.createFormData("imagenes", file.getName(), requestFile));
        }
        RequestBody _id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id));

        Call<Void> resAsync = ApiClient.getRetrofit().createImagenes(ApiClient.getApi().getToken(context), body, _id);
        resAsync.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("salida", "3OnResponse " + response.message() + " " + response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("salida", "3OnFailure " + t.getMessage());
            }
        });
    }

    public void prueba3(File file, int id){
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        RequestBody _id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id));

        Call<Void> resAsync = ApiClient.getRetrofit().publicacionesTest(ApiClient.getApi().getToken(context), _id, body);
        resAsync.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("salida", "3OnResponse " + response.message() + " " + response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("salida", "3OnFailure " + t.getMessage());
            }
        });
    }
}