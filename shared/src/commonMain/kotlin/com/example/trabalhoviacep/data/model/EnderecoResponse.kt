package com.example.trabalhoviacep.data.model

import com.example.trabalhoviacep.domain.model.Endereco
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class EnderecoResponse(
    val cep: String? = null,
    val logradouro: String? = null,
    val bairro: String? = null,
    val localidade: String? = null,
    val uf: String? = null,
    val erro: JsonElement? = null
) {
    val isErro: Boolean
        get() {
            val element = erro ?: return false
            return runCatching {
                val primitive = element.jsonPrimitive
                primitive.booleanOrNull == true || primitive.content.equals("true", ignoreCase = true)
            }.getOrDefault(false)
        }

    fun toDomain(): Endereco {
        return Endereco(
            cep = cep.orEmpty(),
            logradouro = logradouro.orEmpty(),
            bairro = bairro.orEmpty(),
            cidade = localidade.orEmpty(),
            uf = uf.orEmpty()
        )
    }
}