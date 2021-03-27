package com.ethereum.securex.api

import com.ethereum.securex.models.Block
import com.ethereum.securex.models.ObjectResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


var gson: Gson = GsonBuilder()
    .setLenient()
    .create()

val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl("https://ipfs.infura.io:5001/api/v0/")
    .build()

val retrofit2: Retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl("https://ipfs.infura.io:5001/api/v0/")
    .build()

interface ApiHolder {

    @Multipart
    @POST("add?file=/app.txt")
    fun postCredentials(@Part file: MultipartBody.Part): Call<Block>

    @POST("object/data")
    fun getCredentials(@Query("arg") arg: String): Call<String>

}

object Api {
    val retrofitService: ApiHolder by lazy {
        retrofit.create(ApiHolder::class.java)
    }
    val retrofitService2: ApiHolder by lazy {
        retrofit2.create(ApiHolder::class.java)
    }
}