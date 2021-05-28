package com.spartano.tiendamovil.request;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spartano.tiendamovil.model.LoginRequest;
import com.spartano.tiendamovil.model.LoginResponse;
import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.model.Transaccion;
import com.spartano.tiendamovil.model.Usuario;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;

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
