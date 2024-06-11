package com.fithlanz.fithlanz.Screens


import CircularImageWithPicasso
import android.content.Context
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fithlanz.fithlanz.Auth.AuthManager
import com.fithlanz.fithlanz.navigation.AppScreens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.fithlanz.fithlanz.Auth.AuthRes
import com.fithlanz.fithlanz.models.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@Composable
fun PerfilScreen(authManager: AuthManager, navHostController: NavHostController, navHostController2: NavHostController, context: Context,usuario: Usuario?) {
    val scrollState = rememberScrollState()
    usuariologado(authManager = authManager, context = LocalContext.current )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = "Perfil",
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(55.dp))
            CircularImageWithPicasso(
                imageUrl =authManager.Usuario(context = context)?.photoUrl.toString(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                contentDescription = "Descripción de la imagen",
                circleSize = 150.dp // Imagen de perfil más grande
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (usuario != null) {
                Text(
                    text = usuario.username,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Spacer(modifier = Modifier.height(400.dp))
            Button(
                onClick = {
                    authManager.obtenerCliente().signOut()
                    authManager.obtenerautenticacion().signOut()
                    navHostController.navigate(AppScreens.FirstScreen.route)
                    clearCache(context)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Cerrar Sesión",
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Cerrar Sesión", fontSize = 20.sp)
            }
        }

    }

}
@Composable
fun usuariologado(authManager: AuthManager,context: Context) {
    println( authManager.isUserSignedIn(context))
}


fun clearCache(context: Context) {
    try {
        // Obtiene el directorio de caché
        val cacheDir = context.cacheDir

        // Borra los archivos en el directorio de caché
        cacheDir?.let {
            val files = it.listFiles()
            if (files != null) {
                for (file in files) {
                    file.deleteRecursively()
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


