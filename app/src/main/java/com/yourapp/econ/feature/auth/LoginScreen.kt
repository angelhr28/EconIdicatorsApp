package com.yourapp.econ.feature.auth

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yourapp.econ.app.ui.theme.EconTheme
import com.yourapp.econ.feature.auth.state.AuthState

@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    onGoRegister: () -> Unit,
    vm: AuthViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(state.session) { if (state.session != null) onLoggedIn() }

    LoginScaffold(
        state = state,
        onSubmit = vm::onSubmitLogin,
        onGoRegister = onGoRegister,
        onErrorShown = vm::onErrorShown
    )
}

/* ------------------------- UI con Scaffold ------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScaffold(
    state: AuthState,
    onSubmit: (email: String, pass: String) -> Unit,
    onGoRegister: () -> Unit,
    onErrorShown: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it.ifBlank { "Ups, algo salió mal" })
            onErrorShown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("👋 ¡Bienvenido de nuevo!") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LoginContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            state = state,
            onSubmit = onSubmit,
            onGoRegister = onGoRegister
        )
    }
}

/* ------------------------- Contenido puro ------------------------- */

@Composable
private fun LoginContent(
    modifier: Modifier = Modifier,
    state: AuthState,
    onSubmit: (email: String, pass: String) -> Unit,
    onGoRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Box(modifier, contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                placeholder = { Text("ejemplo@email.com") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Contraseña") },
                placeholder = { Text("••••••••") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { onSubmit(email, pass) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.isLoading) "🔄 Iniciando..." else "➡️ Entrar")
            }

            Spacer(Modifier.height(8.dp))

            TextButton(onClick = onGoRegister) {
                Text("¿Aún no tienes cuenta? ✨ Regístrate aquí")
            }
        }
    }
}

/* ------------------------- PREVIEWS ------------------------- */

@Preview(name = "Login - Light", showBackground = true)
@Composable
private fun PreviewLogin_Light() {
    EconTheme {
        LoginScaffold(
            state = AuthState(isLoading = false, session = null, error = null),
            onSubmit = { _, _ -> },
            onGoRegister = {},
            onErrorShown = {}
        )
    }
}

@Preview(
    name = "Login - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewLogin_Dark() {
    EconTheme {
        LoginScaffold(
            state = AuthState(isLoading = false, session = null, error = null),
            onSubmit = { _, _ -> },
            onGoRegister = {},
            onErrorShown = {}
        )
    }
}

@Preview(name = "Login - Loading", showBackground = true)
@Composable
private fun PreviewLogin_Loading() {
    EconTheme {
        LoginScaffold(
            state = AuthState(isLoading = true, session = null, error = null),
            onSubmit = { _, _ -> },
            onGoRegister = {},
            onErrorShown = {}
        )
    }
}

@Preview(name = "Login - Error", showBackground = true)
@Composable
private fun PreviewLogin_Error() {
    EconTheme {
        LoginScaffold(
            state = AuthState(isLoading = false, session = null, error = "Credenciales inválidas"),
            onSubmit = { _, _ -> },
            onGoRegister = {},
            onErrorShown = {}
        )
    }
}
