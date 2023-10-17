package com.dbvertex.loginapi

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiServise {
    @POST("webservice/login")
    fun login(
        @Header("x-api-key") apiKey: String?,  // Add this line
        @Body loginRequest: LoginRequest?
    ): Call<LoginResponse?>
}