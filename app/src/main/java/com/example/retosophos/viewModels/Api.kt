package com.example.retosophos.viewModels

import com.example.retosophos.*
import com.example.retosophos.model.ImageResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query



val retrofit = Retrofit.Builder()
    .baseUrl("https://6w33tkx4f9.execute-api.us-east-1.amazonaws.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val sophosApi = retrofit.create(SophosApi::class.java)

interface SophosApi {


    @GET("RS_Usuarios")
    fun loginUser(@Query("idUsuario") username: String, @Query("clave") password: String): Call<LoginResponse>

    @GET("RS_Oficinas")
    fun loadOffices(): Call<Oficinas>

    @POST("RS_Documentos")
    fun saveDocuments(@Body request: PutDocumentRequest): Call<PutDocumentResponse>

    @GET("RS_Documentos")
    fun loadDocuments(@Query("correo") correo:String): Call<DocumentResponse>


    @GET("RS_Documentos")
    fun getImage(@Query("IdRegistro") IdRegistro:String): Call<ImageResponse>
}


