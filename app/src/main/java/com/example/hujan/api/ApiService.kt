package com.example.hujan.api

import com.example.hujan.response.ResponseCuaca
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("di-yogyakarta/sleman")
    fun getCuaca(): Call<ResponseCuaca>

}