package com.fithlanz.fithlanz.Screens

import android.content.Context
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fithlanz.fithlanz.ViewModel.LogrosViewModel
import com.fithlanz.fithlanz.components.CardWithProgressBar
import com.fithlanz.fithlanz.models.Actividades
import com.fithlanz.fithlanz.models.Usuario


@Composable
fun LogrosScreen(context: Context, usuario: Usuario?, viewModel: LogrosViewModel = viewModel()) {
    val scrollState = rememberLazyListState()
    val listaLogros by viewModel.logros

    LaunchedEffect(Unit) {
        if (listaLogros.isEmpty()) {
            viewModel.obtenerLogros(context)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier.padding(26.dp),
            text = "Logros",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        // Usamos un LazyColumn para mostrar la lista de logros
        LazyColumn(
            modifier = Modifier.weight(1f),
            state = scrollState
        ) {
            items(listaLogros) { logro ->
                val progress = if (logro.type == "Tiempo") {
                    (usuario?.semana?.size?.toFloat()?.div(logro.goal))
                } else {
                    (usuario?.acumulado?.times(0.75f))?.div((logro.goal * 1000))
                }
                progress?.let {
                    CardWithProgressBar(logro,
                        it, Actividades.valueOf(logro.type), context)
                }
            }
        }
    }
}

