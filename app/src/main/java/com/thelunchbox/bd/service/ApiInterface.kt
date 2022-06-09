package com.thelunchbox.bd.service

import com.google.gson.JsonObject
import com.thelunchbox.bd.models.Login
import com.thelunchbox.bd.models.Registration
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface ApiInterface {
    @POST("login/")
    fun getLogin(
        @HeaderMap headers: Map<String?, String?>?,
        @Body locationPost: JsonObject?
    ): Call<Login?>?

    @POST("signup/")
    fun getRegistration(
        @HeaderMap headers: Map<String?, String?>?,
        @Body locationPost: JsonObject?
    ): Call<Registration?>?
}