package com.example.trabalhoviacep

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.trabalhoviacep.ui.CepScreen
import com.example.trabalhoviacep.ui.CepViewModel
import com.example.trabalhoviacep.data.remote.KtorClient
import com.example.trabalhoviacep.data.remote.ViaCepApi
import com.example.trabalhoviacep.data.remote.ViaCepApiImpl
import com.example.trabalhoviacep.data.repository.CepRepositoryImpl
import com.example.trabalhoviacep.domain.repository.CepRepository
import org.koin.compose.KoinApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { KtorClient.client }
    single<ViaCepApi> { ViaCepApiImpl(get()) }
    single<CepRepository> { CepRepositoryImpl(get()) }
    viewModel { CepViewModel(get()) }
}

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        CepScreen()
    }
}