package com.salkinnoma.kotlinApiClientGenerator

import com.salkinnoma.kotlinApiClientGenerator.di.libModule
import com.salkinnoma.kotlinApiClientGenerator.generator.GeneratorRepository
import com.salkinnoma.kotlinApiClientGenerator.setupRequest.SetupRequestRepository
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.core.context.GlobalContext.startKoin
import org.koin.java.KoinJavaComponent.getKoin

class ApiClientGenerator() {
    private lateinit var setupRequestRepository: SetupRequestRepository
    private lateinit var generatorRepository: GeneratorRepository

    init {
        setup()
    }

    fun generateClient(baseUrl: String, query: String?, name: String) {
        val json = setupRequestRepository.getAndParseRequest(baseUrl, query)
        generatorRepository.generateDTO(json, name)
    }

    private fun setup() {
        if (!isKoinStarted()) {
            startKoin {
                modules(libModule)
            }
        } else {
            loadKoinModules(libModule)
        }
        val koin = getKoin()
        generatorRepository = koin.get()
        setupRequestRepository = koin.get()
    }

    private fun isKoinStarted(): Boolean {
        return org.koin.core.context.GlobalContext.getOrNull() != null
    }
}