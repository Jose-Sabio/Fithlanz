package com.fithlanz.fithlanz

import AppNavigation
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fithlanz.fithlanz.Auth.AuthManager
import com.fithlanz.fithlanz.Auth.AuthRes
import com.fithlanz.fithlanz.Volley.TokenCallback
import com.fithlanz.fithlanz.navigation.AppScreens
import com.fithlanz.fithlanz.ui.theme.FithlanzTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

import com.fithlanz.fithlanz.Volley.exchangeAuthCodeForAccessToken
import com.fithlanz.fithlanz.models.Usuario


class InicioSesion : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FithlanzTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                        AppNavigation(this)
                }
            }
        }
    }
}

@Composable
fun IniciarSesion(
    navController: NavController,
    auth: AuthManager,
    context: Context,
    currentUser: Usuario?,
    refreshtoken: MutableState<String>
) {
    var token=""
    val fitnessOptions = auth.getFitnessOptions()
    val scope = rememberCoroutineScope()
    val actividad = context as Activity
    val googleSignInLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        when (val account = auth.handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(result.data))) {
            is AuthRes.Success -> {
                val credential = GoogleAuthProvider.getCredential(account?.data?.idToken, null)
                scope.launch {
                    val fireUser = auth.signInWithGoogleCredential(credential)
                    if (fireUser != null) {
                        val account = GoogleSignIn.getAccountForExtension(context, fitnessOptions)
                        account.serverAuthCode?.let {
                            exchangeAuthCodeForAccessToken(it, context, object :
                                TokenCallback {
                                override fun onTokenReceived(refreshToken: String) {
                                    refreshtoken.value=refreshToken
                                    if (currentUser != null) {
                                        currentUser.refreshtoken=refreshToken
                                    }
                                    Log.d("MainActivity", "Received Refresh Token: $refreshToken")
                                }

                                override fun onError(error: String) {
                                    Log.e("MainActivity", "Error: $error")
                                }
                            })
                        }
                        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
                            GoogleSignIn.requestPermissions(actividad, 1, account, fitnessOptions)
                            if (GoogleSignIn.getLastSignedInAccount(context)==null){
                                Toast.makeText(context,"No se pudo Loguear",Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            GoogleSignIn.requestPermissions(actividad, 1, account, fitnessOptions)
                            if(currentUser!=null){

                            }else{

                            }
                            Toast.makeText(context, "Bienvenido", Toast.LENGTH_SHORT).show()
                            navController.navigate(AppScreens.Registro.route)
                        }
                    }
                }
            }
            is AuthRes.Error -> {
                Toast.makeText(context, "No se pudo loguear:${account.message}", Toast.LENGTH_SHORT).show()
                println(account.message)
            }
            else -> {
                Toast.makeText(context, "No se pudo conectar con auth", Toast.LENGTH_SHORT).show()
            }
        }
    }


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Bienvenido a FitLanz",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 30.dp)
        )
        Button(
            onClick = {
                googleSignInLauncher.launch(auth.googleSignInClient.signInIntent)

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.size(width = 300.dp, height = 45.dp) // Ajusta el tamaño del botón aquí
        ) {
            Text(
                text = "Iniciar Sesión con Google",
                Modifier.padding(end = 13.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            Icon(
                Icons.Rounded.AccountCircle,
                contentDescription = "Iniciar sesión con Google",
            )
        }
    }
}


