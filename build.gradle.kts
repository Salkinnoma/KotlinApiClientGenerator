plugins {
    kotlin("jvm") version "2.1.10"
}

group = "com.salkinnoma.KotlinApiClientGenerator"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //Koin for DI
    implementation("io.insert-koin:koin-core:3.1.2")

    // Retrofit for HTTP requests
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    // Kotlinx Serialization core
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    // OkHttp (used by Retrofit for HTTP handling)
    implementation("com.squareup.okhttp3:okhttp:4.9.2")

    // Kotlin standard library
    implementation(kotlin("stdlib"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(22)
}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(22))
    }
}
sourceSets {
    main {
        java {
            srcDirs("build/generated/ApiClientGenerator") // Include the generated directory as a source directory
        }
    }
}