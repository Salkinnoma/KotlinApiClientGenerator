package com.salkinnoma.kotlinApiClientGenerator.di

import com.salkinnoma.kotlinApiClientGenerator.generator.DtoGenerator
import com.salkinnoma.kotlinApiClientGenerator.generator.GeneratorRepository
import com.salkinnoma.kotlinApiClientGenerator.generator.JsonInspector
import com.salkinnoma.kotlinApiClientGenerator.setupRequest.SetupRequestClient
import com.salkinnoma.kotlinApiClientGenerator.setupRequest.SetupRequestParser
import com.salkinnoma.kotlinApiClientGenerator.setupRequest.SetupRequestRepository
import org.koin.core.module.Module
import org.koin.dsl.module

val libModule: Module = module {
    single { SetupRequestClient() }

    single { SetupRequestParser() }

    single { SetupRequestRepository(get(), get()) }

    single { JsonInspector() }

    single { DtoGenerator() }

    single { GeneratorRepository(get(), get()) }
}