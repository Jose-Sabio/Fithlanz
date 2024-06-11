package com.fithlanz.fithlanz.Screens

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fithlanz.fithlanz.ViewModel.ClasificacionViewModel
import com.fithlanz.fithlanz.components.ClassificationRow
import com.fithlanz.fithlanz.models.Usuario

@Composable
fun ComunidadScreen(context: Context, viewModel: ClasificacionViewModel = viewModel()) {
    val scrollState = rememberScrollState()
    val clasificacion by viewModel.clasi

    LaunchedEffect(Unit) {
        if (clasificacion.isEmpty()) {
            viewModel.obtenerClasificacion(context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Comunidad",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        clasificacion.forEachIndexed { index, usuario ->
            ClassificationRow(
                rank = (index + 1).toString(),
                userName = usuario.username,
                imageUrl = usuario.profileImg,
                contentDescription = "Imagen de perfil de ${usuario.username}"
            )
        }
    }
}
