package com.example.trabalhoviacep.data.repository

import com.example.trabalhoviacep.data.remote.ViaCepApi
import com.example.trabalhoviacep.domain.model.Endereco
import com.example.trabalhoviacep.domain.repository.CepRepository

class CepRepositoryImpl(
    private val api: ViaCepApi
) : CepRepository {

    override suspend fun buscarCep(cep: String): Result<Endereco> {
        return runCatching {
            val response = api.buscarCep(cep)
            if (response.isErro) {
                throw NoSuchElementException("CEP não encontrado.")
            }
            response.toDomain()
        }
    }
}