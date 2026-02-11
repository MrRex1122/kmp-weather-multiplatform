package com.example.kotlinpet.network

import io.ktor.client.HttpClient

expect fun createHttpClient(): HttpClient
