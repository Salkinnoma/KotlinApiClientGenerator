package com.salkinnoma.kotlinApiClientGenerator

fun main() {
    val generator = ApiClientGenerator()
    generator.generateClient("https://reqres.in/api/users/", "?page=2/", "reqrestListUsers")
    generator.generateClient("https://reqres.in/api/unknown/2", null, "reqresListResources")
}