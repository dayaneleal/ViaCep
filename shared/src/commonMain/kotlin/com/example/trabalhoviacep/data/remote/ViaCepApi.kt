package com.example.trabalhoviacep.data.remote

import com.example.trabalhoviacep.data.model.EnderecoResponse

interface ViaCepApi {
    suspend fun buscarCep(cep: String): EnderecoResponse
}