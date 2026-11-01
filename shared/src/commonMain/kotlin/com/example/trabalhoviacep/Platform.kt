package com.example.trabalhoviacep

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform