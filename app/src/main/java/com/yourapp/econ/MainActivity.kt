package com.yourapp.econ

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yourapp.econ.app.ui.theme.EconTheme
import com.yourapp.econ.feature.auth.LoginScreen
import com.yourapp.econ.feature.auth.RegisterScreen
import com.yourapp.econ.feature.indicators.IndicatorsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EconTheme {
                val nav = rememberNavController()
                val snackHost = remember { SnackbarHostState() }
                Surface {
                    SnackbarHost(hostState = snackHost)
                    NavHost(navController = nav, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                onLoggedIn = {
                                    nav.navigate("indicators") {
                                        popUpTo("login") {
                                            inclusive = true
                                        }
                                    }
                                },
                                onGoRegister = { nav.navigate("register") }
                            )
                        }
                        composable("register") {
                            RegisterScreen(onRegistered = { nav.popBackStack() })
                        }
                        composable("indicators") {
                            IndicatorsScreen(onLogout = { nav.navigate("login") { popUpTo(0) } })
                        }
                    }
                }
            }
        }
    }
}
