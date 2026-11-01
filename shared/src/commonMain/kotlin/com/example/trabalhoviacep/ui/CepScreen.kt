package com.example.trabalhoviacep.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.trabalhoviacep.domain.model.Endereco
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CepScreen(
    modifier: Modifier = Modifier,
    viewModel: CepViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cepInput  by viewModel.cepInput.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiState) {
        val currentState = uiState
        if (currentState is CepUiState.Error) {
            snackbarHostState.showSnackbar(
                message = currentState.mensagem,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                enabled = uiState !is CepUiState.Loading,
                value = cepInput,
                onValueChange =  viewModel::onCepChange,
                label = { Text("Digite o CEP") },
                placeholder = { Text("Ex: 01001-000") },
                singleLine = true,
                visualTransformation = CepVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        viewModel.buscarCep()
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            ElevatedButton(
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    viewModel.buscarCep()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is CepUiState.Loading
            ) {
                if (uiState is CepUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.5.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(text = "Buscar")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                AnimatedContent(
                    targetState = uiState,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "CepUiStateAnimation"
                ) { state ->
                    when (state) {
                        is CepUiState.Idle, is CepUiState.Error,  is CepUiState.Loading -> EnderecoContent(endereco = null)
                        is CepUiState.Success ->  EnderecoContent(endereco = state.endereco)
                    }
                }
            }
        }
    }
}

@Composable
private fun EnderecoContent(endereco: Endereco?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        EnderecoItem(label = "CEP", valor = endereco?.let { formatarCep(it.cep) }.orEmpty())
        EnderecoItem(label = "Logradouro", valor = endereco?.logradouro.orEmpty())
        EnderecoItem(label = "Bairro", valor = endereco?.bairro.orEmpty())
        EnderecoItem(label = "Localidade", valor = endereco?.cidade.orEmpty())
        EnderecoItem(label = "UF", valor = endereco?.uf.orEmpty())
    }
}

@Composable
private fun EnderecoItem(label: String, valor: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label: $valor",
        )
    }
}

private fun formatarCep(cep: String): String {
    val digitsOnly = cep.filter { it.isDigit() }
    return if (digitsOnly.length == 8) {
        "${digitsOnly.substring(0, 5)}-${digitsOnly.substring(5)}"
    } else {
        cep
    }
}

@Preview(showBackground = true)
@Composable
fun SuccessPreview() {
    MaterialTheme {
        EnderecoContent(
            endereco = Endereco(
                cep = "01001-000",
                logradouro = "Praça da Sé",
                bairro = "Sé",
                cidade = "São Paulo",
                uf = "SP"
            )
        )
    }
}
