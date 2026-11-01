package com.example.trabalhoviacep.data.remote

import com.example.trabalhoviacep.data.model.EnderecoResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ViaCepApiImpl(
    private val client: HttpClient = KtorClient.client
) : ViaCepApi {
    override suspend fun buscarCep(cep: String): EnderecoResponse {
        return client.get("https://viacep.com.br/ws/$cep/json").body()
    }
}