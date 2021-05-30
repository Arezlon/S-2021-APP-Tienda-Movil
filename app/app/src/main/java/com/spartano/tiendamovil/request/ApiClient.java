package com.spartano.tiendamovil.request;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spartano.tiendamovil.model.LoginRequest;
import com.spartano.tiendamovil.model.LoginResponse;
import com.spartano.tiendamovil.model.Publicacion;
import com.spartano.tiendamovil.model.PublicacionImagen;
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
import retrofit2.http.DELETE;
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
        @POST("usuarios/login") Call<LoginResponse> login(@Body LoginRequest loginRequest);
        @POST("usuarios/create") Call<Boolean> createUsuario(@Body Usuario usuario);
        @PUT("usuarios/edit") Call<Void> editUsuario(@Body Usuario usuario, @Header("Authorization") String token);
        @GET("usuarios/get") Call<Usuario> getUsuario(@Header("Authorization") String token);

        //Publicaciones
        @POST("publicaciones/create") Call<Void> createPublicacion(@Body Publicacion publicacion, @Header("Authorization") String token);
        @GET("publicaciones/getmias") Call<List<Publicacion>> getMisPublicaciones(@Header("Authorization") String token);
        @GET("publicaciones/get") Call<List<Publicacion>> getPublicacionesDestacadas(@Header("Authorization") String token);
        @GET("publicaciones/getcategorias") Call<Map<Integer, String>> getCategoriasPublicaciones(@Header("Authorization") String token);
        @GET("publicaciones/gettipos") Call<Map<Integer, String>> getTiposPublicaciones(@Header("Authorization") String token);
        @PUT("publicaciones/edit") Call<Void> editPublicacion(@Body Publicacion publicacion, @Header("Authorization") String token);

        // Publicaciones>Imágenes
        @Multipart
        @POST("publicacionimagenes/create") Call<Void> createImagenes(@Header("Authorization") String token, @Part Collection<MultipartBody.Part> imagenes, @Part("publicacionId") RequestBody id);
        @GET("publicacionimagenes/get") Call<List<PublicacionImagen>> getImagenes(@Header("Authorization") String token, @Query("publicacionId") int publicacionId);
        @DELETE("publicacionimagenes/delete") Call<Void> deleteImagen(@Header("Authorization") String token, @Query("imagenId") int imagenId);
        @PATCH("publicacionimagenes/destacar") Call<Void> destacarImagen(@Header("Authorization") String token, @Body PublicacionImagen imagen);

        //Transacciones
        @POST("transacciones/create") Call<Void> createTransaccion(@Body Transaccion transaccion, @Header("Authorization") String token);
        @GET("transacciones/get") Call<List<Transaccion>> getTransacciones(@Header("Authorization") String token);
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
