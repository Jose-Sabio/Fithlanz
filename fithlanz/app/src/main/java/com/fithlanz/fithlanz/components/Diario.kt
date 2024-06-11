package com.fithlanz.fithlanz.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgressBarWithText(
    progress: Float,
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        // Circulo externo
        CircularProgressIndicator(
            progress = progress,
            color= MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(210.dp)
        )
        // Texto dentro del circulo interno
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary, // Utiliza el color de texto en el tema
            style = MaterialTheme.typography.titleLarge, // Utiliza el estilo de texto del tema
            modifier = Modifier.padding(8.dp) // Agrega un relleno al texto
        )
    }
}

@Preview
@Composable
fun CircularProgressBarWithTextPreview() {
    CircularProgressBarWithText(progress = 0.40f, text = "7000\nPasos", Modifier)
}
