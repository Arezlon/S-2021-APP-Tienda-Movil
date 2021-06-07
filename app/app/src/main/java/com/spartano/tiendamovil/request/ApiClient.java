package com.spartano.tiendamovil.request;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spartano.tiendamovil.model.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

public class ApiClient {
    private static ApiClient api = null;
    //private static final String PATH="http://192.168.1.106:45456/api/"; //Diego
    //private static final String PATH="http://192.168.1.104:45456/api/"; //Sebastian

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
        @GET("publicaciones/getbyid") Call<Publicacion> getPublicacion(@Header("Authorization") String token, @Query("id") int id);
        @GET("publicaciones/get") Call<List<Publicacion>> getPublicacionesUsuario(@Header("Authorization") String token, @Query("usuarioId") int usuarioId);
        @GET("publicaciones/getdestacadas") Call<List<Publicacion>> getPublicacionesDestacadas(@Header("Authorization") String token);
        @GET("publicaciones/getrecomendadas") Call<List<Publicacion>> getPublicacionesRecomendadas(@Header("Authorization") String token);
        @GET("publicaciones/getcategorias") Call<Map<Integer, String>> getCategoriasPublicaciones(@Header("Authorization") String token);
        @GET("publicaciones/gettipos") Call<Map<Integer, String>> getTiposPublicaciones(@Header("Authorization") String token);
        @GET("publicaciones/buscar") Call<List<Publicacion>> buscarPublicaciones(@Header("Authorization") String token,
                                                                                 @Query("busqueda") String busqueda,
                                                                                 @Query("precioMaximo") float precioMaximo,
                                                                                 @Query("categoria") int categoria,
                                                                                 @Query("estado") int estado);
        @PUT("publicaciones/edit") Call<Void> editPublicacion(@Body Publicacion publicacion, @Header("Authorization") String token);

        // Publicaciones>Imágenes
        @Multipart
        @POST("publicacionimagenes/create") Call<Void> createImagenes(@Header("Authorization") String token, @Part Collection<MultipartBody.Part> imagenes, @Part("publicacionId") RequestBody id);
        @GET("publicacionimagenes/get") Call<List<PublicacionImagen>> getImagenes(@Header("Authorization") String token, @Query("publicacionId") int publicacionId);
        @DELETE("publicacionimagenes/delete") Call<Void> deleteImagen(@Header("Authorization") String token, @Query("imagenId") int imagenId);
        @PATCH("publicacionimagenes/destacar") Call<Void> destacarImagen(@Header("Authorization") String token, @Body PublicacionImagen imagen);

        // Publicaciones>Comentarios
        @GET("comentarios/get") Call<List<Comentario>> getComentarios(@Header("Authorization") String token, @Query("publicacionId") int publicacionId);
        @POST("comentarios/create") Call<Void> createComentario(@Header("Authorization") String token, @Body Comentario comentario);
        @PATCH("comentarios/patch") Call<Void> responderComentario(@Header("Authorization") String token, @Body Comentario comentario);

        // Publicaciones>Reseñas
        @GET("reseñas/get") Call<List<Reseña>> getReseñas(@Header("Authorization") String token, @Query("publicacionId") int publicacionId);
        @POST("reseñas/create") Call<Void> createReseña(@Header("Authorization") String token, @Body Reseña reseña);
        @GET("reseñas/comprobarReseña") Call<Boolean> comprobarReseña(@Header("Authorization") String token, @Query("publicacionId") int publicacionId);

        // Publicaciones>Etiquetas
        @GET("publicacionetiquetas/get") Call<List<PublicacionEtiqueta>> getEtiquetas(@Header("Authorization") String token, @Query("publicacionId") int publicacionId);
        @POST("publicacionetiquetas/create") Call<Void> createEtiquetas(@Header("Authorization") String token, @Body List<PublicacionEtiqueta> publicacionEtiquetas);
        @DELETE("publicacionetiquetas/delete") Call<Void> deleteEtiqueta(@Header("Authorization") String token, @Query("publicacionEtiquetaId") int publicacionEtiquetaId);

        //Transacciones
        @POST("transacciones/create") Call<Void> createTransaccion(@Body Transaccion transaccion, @Header("Authorization") String token);
        @GET("transacciones/get") Call<List<Transaccion>> getTransacciones(@Header("Authorization") String token);

        //Compras
        @POST("compras/create") Call<Void> createCompra(@Body Compra compra, @Header("Authorization") String token);
        @GET("compras/getultima") Call<Compra> getUltima(@Header("Authorization") String token);
        @GET("compras/get") Call <List<Compra>> getCompras(@Header("Authorization") String token);
        @GET("compras/getVentas") Call <List<Compra>> getVentas(@Header("Authorization") String token);
        @GET("compras/comprobarusuario") Call <Boolean> comprobarUsuario(@Header("Authorization") String token, @Query("compraId") int compraId);

        //Notificaciones
        @GET("notificaciones/get") Call <List<Notificacion>> getNotificaciones(@Header("Authorization") String token);
        @PATCH("notificaciones/patch") Call<Void> leerNotificacion(@Header("Authorization") String token, @Body Notificacion notificacion);
        @GET("notificaciones/gettotal") Call <Integer> getCantidadNotificaciones(@Header("Authorization") String token);
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
