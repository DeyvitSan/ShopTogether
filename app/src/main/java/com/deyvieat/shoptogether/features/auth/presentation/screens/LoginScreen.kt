package com.deyvieat.shoptogether.features.auth.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment; import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*; import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deyvieat.shoptogether.features.auth.presentation.viewmodels.AuthViewModel
import kotlinx.coroutines.flow.collectLatest


@Composable

fun LoginScreen(onSuccess: () -> Unit, onGoRegister:() -> Unit, vm: AuthViewModel = hiltViewModel()){

    val uiState by vm.uiState.collectAsStateWithLifecycle()

    val snackbar = remember { SnackbarHostState() }

    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState.isSuccess) {if (uiState.isSuccess) onSuccess() }

    LaunchedEffect(Unit) { vm.events.collectLatest { snackbar.showSnackbar(it) } }

    Scaffold(snackbarHost = { SnackbarHost(snackbar) }) { pad ->
        Column(
            Modifier.fillMaxSize()
                .padding(pad)
                .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center){

            Text("SubastaLive ", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(32.dp))

            OutlinedTextField(value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true)
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true)
            Spacer(Modifier.height(24.dp))

            Button(onClick = { vm.login(email, password) }, modifier = Modifier.fillMaxWidth(), enabled = !uiState.isLoading) {
                if (uiState.isLoading) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                else Text("Entrar")
            }
            Spacer(Modifier.height(12.dp))

            TextButton(onClick = onGoRegister) { Text("¿No tienes cuenta? Regístrate") }
        }
    }
}