package com.salkinnoma.kotlinApiClientGenerator

fun main() {
    val generator = ApiClientGenerator()
    generator.generateClient("https://reqres.in/api/users/", "?page=2/", "reqrestListUsers")
}