package com.example.trabalhoviacep.ui

import com.example.trabalhoviacep.domain.model.Endereco

sealed interface CepUiState {
    data object Idle : CepUiState
    data object Loading : CepUiState
    data class Success(val endereco: Endereco) : CepUiState
    data class Error(val mensagem: String) : CepUiState
}