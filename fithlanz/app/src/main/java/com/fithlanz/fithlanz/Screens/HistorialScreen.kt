package com.fithlanz.fithlanz.Screens

import HikingCard
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fithlanz.fithlanz.models.Usuario

@Composable
fun HistorialScreen(navController: NavHostController, usuario: Usuario?) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "Historial",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        usuario?.semana?.reversed()?.forEach { dia -> // Mostrar la lista de la semana al revés
            HikingCard(dia)
            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre las HikingCards
        }
    }
}



