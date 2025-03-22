package com.salkinnoma.kotlinApiClientGenerator

import com.salkinnoma.kotlinApiClientGenerator.di.libModule
import com.salkinnoma.kotlinApiClientGenerator.setupRequest.SetupRequestRepository
import kotlinx.serialization.json.JsonNull
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.core.context.GlobalContext.startKoin
import org.koin.java.KoinJavaComponent.getKoin

class ApiClientGenerator(baseUrl: String, query: String?) {
    private var setupRequestRepository: SetupRequestRepository

    init {
        setup()
        setupRequestRepository = getKoin().get()
        val json = setupRequestRepository.getAndParseRequest(baseUrl, query)
        if (json is JsonNull) {
            val exceptionMessage = "Api request for $baseUrl${query?.let { "?$it" } ?: ""} is null"
            throw IllegalArgumentException(exceptionMessage)
        }
    }

    private fun setup() {
        if (!isKoinStarted()) {
            startKoin {
                modules(libModule)
            }
        } else {
            loadKoinModules(libModule)
        }
    }

    private fun isKoinStarted(): Boolean {
        return org.koin.core.context.GlobalContext.getOrNull() != null
    }
}