package com.salkinnoma.kotlinApiClientGenerator.setupRequest

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface SetupRequestServiceInterface {
    @GET
    fun getWithQuery(@Url url: String): Call<ResponseBody>

    @GET
    fun getWithoutQuery(@Url url: String) : Call<ResponseBody>
}