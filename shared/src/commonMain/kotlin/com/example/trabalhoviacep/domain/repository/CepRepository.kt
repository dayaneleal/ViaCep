package com.example.trabalhoviacep.domain.repository

import com.example.trabalhoviacep.domain.model.Endereco

interface CepRepository {
    suspend fun buscarCep(cep: String): Result<Endereco>

    @Throws(Exception::class)
    suspend fun buscarCepOrThrow(cep: String): Endereco = buscarCep(cep).getOrThrow()
}