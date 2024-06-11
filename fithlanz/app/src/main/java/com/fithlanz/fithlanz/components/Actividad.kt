import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Error
import androidx.compose.ui.graphics.Color
import com.fithlanz.fithlanz.parseadores.DiaItem
import com.fithlanz.fithlanz.ui.theme.FithlanzTheme
@Composable
fun HikingCard(diaItem: DiaItem) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Card para el turno 1
        Card(
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "${diaItem.dia}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.surface                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Utiliza el icono adecuado según el tipo de actividad del día
                        val iconTurno1 = when {
                            diaItem.actividad.turno1.any { it.tipo == "A pie" } -> Icons.Filled.DirectionsWalk
                            diaItem.actividad.turno1.any { it.tipo == "Bicicleta" } -> Icons.Filled.DirectionsBike
                            else -> Icons.Filled.Error // Icono de error si el tipo de actividad no está reconocido
                        }
                        Icon(
                            imageVector = iconTurno1,
                            contentDescription = "Activity Icon",
                            modifier = Modifier.size(40.dp),
                            tint=MaterialTheme.colorScheme.surface
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Actividad (07:30 - 08:40): ${diaItem.actividad.turno1.firstOrNull()?.tipo}",
                                style = MaterialTheme.typography.titleMedium
                                , color = MaterialTheme.colorScheme.surface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            // Muestra la distancia en metros con 2 decimales si está presente en el primer turno de actividad
                            diaItem.actividad.turno1.firstOrNull()?.distancia?.let {
                                val distanciaMetros =
                                    String.format("%.2f", it) // Convertir a metros y formatear
                                Text(
                                    text = "Distancia : ${distanciaMetros} m",
                                    style = MaterialTheme.typography.bodyLarge,
                                     color = MaterialTheme.colorScheme.surface
                                )
                            }
                            // Muestra el tiempo en minutos sin decimales si está presente en el primer turno de actividad
                            diaItem.actividad.turno1.firstOrNull()?.tiempo?.let {
                                val tiempoMinutos =
                                    it.substringBefore("h").toFloat()  // Convertir a minutos
                                Text(
                                    text = "Duración : ${tiempoMinutos.toInt()} minutos",
                                    style = MaterialTheme.typography.bodyMedium,
                                     color = MaterialTheme.colorScheme.surface
                                )
                            }
                        }
                    }
                }
            }
        }
        // Card para el turno 2
        Card(
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = " ${diaItem.dia}",
                        style = MaterialTheme.typography.titleMedium
                        , color = MaterialTheme.colorScheme.surface                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Utiliza el icono adecuado según el tipo de actividad del día
                        val iconTurno2 = when {
                            diaItem.actividad.turno2.any { it.tipo == "A pie" } -> Icons.Filled.DirectionsWalk
                            diaItem.actividad.turno2.any { it.tipo == "Bicicleta" } -> Icons.Filled.DirectionsBike
                            else -> Icons.Filled.Error // Icono de error si el tipo de actividad no está reconocido
                        }
                        Icon(
                            imageVector = iconTurno2,
                            contentDescription = "Activity Icon",
                            modifier = Modifier.size(40.dp),
                            tint =  MaterialTheme.colorScheme.surface

                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Actividad (14:45 - 15:30): ${diaItem.actividad.turno2.firstOrNull()?.tipo}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.surface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            // Muestra la distancia en metros con 2 decimales si está presente en el segundo turno de actividad
                            diaItem.actividad.turno2.firstOrNull()?.distancia?.let {
                                val distanciaMetros =
                                    String.format("%.2f", it) // Convertir a metros y formatear
                                Text(
                                    text = "Distancia : ${distanciaMetros} m",
                                    style = MaterialTheme.typography.bodyMedium
                                    , color = MaterialTheme.colorScheme.surface
                                )
                            }
                            // Muestra el tiempo en minutos sin decimales si está presente en el segundo turno de actividad
                            diaItem.actividad.turno2.firstOrNull()?.tiempo?.let {
                                val tiempoMinutos =
                                    it.substringBefore("h").toFloat()  // Convertir a minutos
                                Text(
                                    text = "Duración : ${tiempoMinutos.toInt()} minutos",
                                    style = MaterialTheme.typography.bodyMedium,
                                     color = MaterialTheme.colorScheme.surface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

