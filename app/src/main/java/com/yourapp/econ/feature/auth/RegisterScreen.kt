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
fun RegisterScreen(
    onRegistered: () -> Unit,
    vm: AuthViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(state.session) { if (state.session != null) onRegistered() }

    RegisterScaffold(
        state = state,
        onSubmit = vm::onSubmitRegister,
        onErrorShown = vm::onErrorShown
    )
}

/* ------------------------- UI con Scaffold ------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterScaffold(
    state: AuthState,
    onSubmit: (email: String, name: String, last: String, pass: String) -> Unit,
    onErrorShown: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it.ifBlank { "Ups, algo salió mal 😅" })
            onErrorShown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("📝 Crea tu cuenta") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        RegisterContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            state = state,
            onSubmit = onSubmit
        )
    }
}

/* ------------------------- Contenido puro ------------------------- */

@Composable
private fun RegisterContent(
    modifier: Modifier = Modifier,
    state: AuthState,
    onSubmit: (email: String, name: String, last: String, pass: String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var last by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Box(modifier, contentAlignment = Alignment.Center) {
        Column(
            Modifier.fillMaxWidth(),
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
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                placeholder = { Text("Tu nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = last,
                onValueChange = { last = it },
                label = { Text("Apellido") },
                placeholder = { Text("Tu apellido") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Elige una contraseña (mín. 6 caracteres)") },
                placeholder = { Text("••••••••") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { onSubmit(email, name, last, pass) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.isLoading) "🚀 Creando cuenta..." else "✨ Registrarme")
            }
        }
    }
}

/* ------------------------- PREVIEWS ------------------------- */

@Preview(name = "Registro - Light", showBackground = true)
@Composable
private fun PreviewRegister_Light() {
    EconTheme {
        RegisterScaffold(
            state = AuthState(isLoading = false, session = null, error = null),
            onSubmit = { _, _, _, _ -> },
            onErrorShown = {}
        )
    }
}

@Preview(
    name = "Registro - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewRegister_Dark() {
    EconTheme {
        RegisterScaffold(
            state = AuthState(isLoading = false, session = null, error = null),
            onSubmit = { _, _, _, _ -> },
            onErrorShown = {}
        )
    }
}

@Preview(name = "Registro - Loading", showBackground = true)
@Composable
private fun PreviewRegister_Loading() {
    EconTheme {
        RegisterScaffold(
            state = AuthState(isLoading = true, session = null, error = null),
            onSubmit = { _, _, _, _ -> },
            onErrorShown = {}
        )
    }
}

@Preview(name = "Registro - Error", showBackground = true)
@Composable
private fun PreviewRegister_Error() {
    EconTheme {
        RegisterScaffold(
            state = AuthState(isLoading = false, session = null, error = "Correo ya registrado"),
            onSubmit = { _, _, _, _ -> },
            onErrorShown = {}
        )
    }
}
