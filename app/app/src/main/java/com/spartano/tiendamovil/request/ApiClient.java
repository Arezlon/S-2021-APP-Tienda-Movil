package com.spartano.tiendamovil.request;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spartano.tiendamovil.model.LoginRequest;
import com.spartano.tiendamovil.model.LoginResponse;
import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.model.Transaccion;
import com.spartano.tiendamovil.model.Usuario;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public class ApiClient {
    private static ApiClient api = null;
    //private static final String PATH="http://192.168.0.107:45455/api/"; //Diego
    //private static final String PATH="http://192.168.0.108:45455/api/"; //Sebastian

    private static MyRetrofit myRetrofit;

    public static MyRetrofit getRetrofit(){
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(PATH)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        myRetrofit = retrofit.create(MyRetrofit.class);
        return myRetrofit;
    }

    public static String getPath(){
        return PATH.substring(0,26); // Excluir /api/
    }

    public interface MyRetrofit {
        //Usuarios
        @POST("usuarios/login")
        public Call<LoginResponse> login(@Body LoginRequest loginRequest);

        @POST("usuarios/create")
        public Call<Boolean> createUsuario(@Body Usuario usuario);

        @PUT("usuarios/edit")
        public Call<Void> editUsuario(@Body Usuario usuario, @Header("Authorization") String token);

        @GET("usuarios/get")
        public Call<Usuario> getUsuario(@Header("Authorization") String token);

        //Publicaciones
        @Multipart
        @POST("publicaciones/createimagenes")
        public Call<Void> createImagenes(@Header("Authorization") String token,
                                         //@Part MultipartBody.Part[] imagenes,
                                         @Part Collection<MultipartBody.Part> imagenes,
                                         @Part("publicacionId") RequestBody id);

        @Multipart
        @PUT("publicaciones/test")
        public Call<Void> publicacionesTest(@Header("Authorization") String token,
                                            @Part("id") RequestBody id,
                                            @Part MultipartBody.Part image);

        @POST("publicaciones/create")
        public Call<Void> createPublicacion(@Body Publicacion publicacion, @Header("Authorization") String token);

        @GET("publicaciones/getmias")
        public Call<List<Publicacion>> getMisPublicaciones(@Header("Authorization") String token);

        @GET("publicaciones/getcategorias")
        public Call<Map<Integer, String>> getCategoriasPublicaciones(@Header("Authorization") String token);

        @GET("publicaciones/gettipos")
        public Call<Map<Integer, String>> getTiposPublicaciones(@Header("Authorization") String token);

        @PUT("publicaciones/edit")
        public Call<Void> editPublicacion(@Body Publicacion publicacion, @Header("Authorization") String token);

        //Transacciones
        @POST("transacciones/create")
        public Call<Void> createTransaccion(@Body Transaccion transaccion, @Header("Authorization") String token);

        @GET("transacciones/get")
        public Call<List<Transaccion>> getTransacciones(@Header("Authorization") String token);
    }

    public String getToken(Context context) {
        String token = context.getSharedPreferences("data.dat", 0).getString("token", "Error al recuperar el token");
        return token;
    }

    public static ApiClient getApi(){
        if (api == null){
            api = new ApiClient();
        }
        return api;
    }
}
