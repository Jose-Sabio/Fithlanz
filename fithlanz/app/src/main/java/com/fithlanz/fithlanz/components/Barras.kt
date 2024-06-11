package com.fithlanz.fithlanz.components
/*
import HikingCard
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fithlanz.fithlanz.Screens.obtenerListaDivisionSemana
import com.fithlanz.fithlanz.models.Usuario
import com.fithlanz.fithlanz.parseadores.DiaItem

@Composable
fun HistorialCard(usuario: Usuario) {
    val diasDeLaSemana = listOf("L", "M", "X", "J", "V")

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(300.dp) // Aquí puedes ajustar la altura del Card según tu preferencia
            .clip(RoundedCornerShape(8.dp)),
        backgroundColor = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                usuario.semana.forEachIndexed { index, semana ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Barra(
                            tamaño = obtenerTamañoBarra(semana),
                            onClick = { mostrarDetallesDia(semana) } // Pasar el día al hacer clic en la barra
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = diasDeLaSemana[index],
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface // Color de texto del tema
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Barra(tamaño: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp, tamaño.dp)
            .clickable(onClick = onClick)
            .background(color = Color(android.graphics.Color.parseColor("#DC5727")))
    )
}

@Composable
fun mostrarDetallesDia(diaItem: DiaItem) {
    // Lógica para mostrar los detalles del día utilizando HikingCard
    HikingCard(diaItem)
}

@Composable
fun obtenerTamañoBarra(diaItem: DiaItem): Int {
    // Lógica para calcular el tamaño de la barra según algún criterio en función de los datos del día
    // Por ejemplo, puedes usar la distancia recorrida, el tiempo de actividad, etc.
    // Aquí devuelvo un tamaño fijo para demostración
    return 100
}

*/