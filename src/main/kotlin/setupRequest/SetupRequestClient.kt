package com.salkinnoma.kotlinApiClientGenerator.setupRequest

import retrofit2.Retrofit

class SetupRequestClient {

    fun doRequest(baseUrl: String, query: String?) : String {
        val url = if (!baseUrl.endsWith("/")) {
            "$baseUrl/"
        } else {
            baseUrl
        }
        val retrofit: Retrofit =
            Retrofit.Builder()
                .baseUrl(url)
                .build()

        val service = retrofit.create(SetupRequestServiceInterface::class.java)
        val response = if (query.isNullOrEmpty()) {
            service.getWithoutQuery().execute()
        } else {
            service.getWithQuery(query).execute()
        }

        return response.body()?.string() ?: ""
    }
}