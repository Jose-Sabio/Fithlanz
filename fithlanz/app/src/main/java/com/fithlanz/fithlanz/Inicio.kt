package com.fithlanz.fithlanz



import BottomBar
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.util.copy
import com.fithlanz.fithlanz.Auth.AuthManager
import com.fithlanz.fithlanz.Screens.ComunidadScreen
import com.fithlanz.fithlanz.Screens.HistorialScreen
import com.fithlanz.fithlanz.Screens.HomeScreen
import com.fithlanz.fithlanz.Screens.LogrosScreen
import com.fithlanz.fithlanz.Screens.PerfilScreen
import com.fithlanz.fithlanz.models.Logro
import com.fithlanz.fithlanz.models.Usuario
import com.fithlanz.fithlanz.navigation.AppScreens
import obtenerLogros
import obtenerclasificacion
import startDestinationChoice

@Composable
fun InicioSesion2(
    navController: NavHostController,
    authmanager: AuthManager,
    context: Context
) {
    var currentUser by remember { mutableStateOf<Usuario?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        startDestinationChoice(context, true,authmanager,) { route, usuario ->
            currentUser = usuario
            isLoading = false
        }
    }

    if (!isLoading) {
        val navController2 = rememberNavController()
        val listaLogros = remember { mutableStateOf(emptyList<Logro>()) }
        val usuarios = remember { mutableStateOf(emptyList<Usuario>()) }
        val buttonsVisible = remember { mutableStateOf(true) }

        // LaunchedEffect para obtener la lista de logros
        LaunchedEffect(Unit) {
            obtenerLogros(context) { listaLogro ->
                listaLogros.value = listaLogro
            }
        }

        // LaunchedEffect para obtener la clasificación de usuarios
        LaunchedEffect(Unit) {
            obtenerclasificacion(context) { listaUsuarios ->
                usuarios.value = listaUsuarios
            }
        }

        Scaffold(
            bottomBar = {
                if (buttonsVisible.value) {
                    BottomBar(navController = navController2, buttonsVisible)
                }
            }
        ) { paddingValues ->
            // Contenido de la pantalla con relleno
            Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                NavHost(navController2, AppScreens.Comunidad.route) {
                    composable(AppScreens.Diario.route) {
                        buttonsVisible.value = true
                        HomeScreen(navController2, currentUser)
                    }
                    composable(AppScreens.Retos.route) {
                        buttonsVisible.value = true
                        LogrosScreen(context, currentUser)
                    }
                    composable(AppScreens.Perfil.route) {
                        buttonsVisible.value = true
                        PerfilScreen(authmanager, navController, navController2, context, currentUser)
                    }
                    composable(AppScreens.Comunidad.route) {
                        buttonsVisible.value = true
                        ComunidadScreen(context)
                    }
                    composable(AppScreens.Historial.route) {
                        buttonsVisible.value = false // Ocultar BottomBar en HistorialScreen
                        HistorialScreen(navController2, currentUser)
                    }
                }
            }
        }
    }
}
