package com.fithlanz.fithlanz.Screens

import LineGraphCard
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fithlanz.fithlanz.components.CircularProgressBarWithText
import com.fithlanz.fithlanz.models.SemanaItem
import com.fithlanz.fithlanz.models.Usuario
import com.fithlanz.fithlanz.navigation.AppScreens
import com.fithlanz.fithlanz.parseadores.DiaItem

@Composable
fun HomeScreen(navController: NavHostController, usuario: Usuario?) {
    val scrollState = rememberScrollState()
    val acumuladoultimo = usuario?.semana?.let { obtenerSumaAcumuladoUltimoDia(it) }
    val listaDivisionSemana = usuario?.diario?.let { obtenerListaDivisionSemana(it) }

    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(scrollState).background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Diario",
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.
            padding(16.dp),
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(95.dp).align(Alignment.CenterHorizontally))
        if (acumuladoultimo != null) {
            CircularProgressBarWithText(
                progress = (acumuladoultimo / 5000).toFloat(),
                text = acumuladoultimo.toString()+"\npasos",
                modifier = Modifier.align(Alignment.CenterHorizontally) // Centrar horizontalmente
            )
        }
        Spacer(modifier = Modifier.height(95.dp))
        if (listaDivisionSemana != null) {
            LineGraphCard(
                points = listaDivisionSemana,
                onClick = { navController.navigate(AppScreens.Historial.route) }
            )
        }
    }
}

fun obtenerSumaAcumuladoUltimoDia(dias: List<DiaItem>): Int {
    // Verifica si la lista de días está vacía
    if (dias.isEmpty()) return 0

    // Obtiene el último elemento de la lista de días
    val ultimoDia = dias.last()

    // Obtiene los valores de acumulado de los dos turnos del último día
    val acumuladoTurno1 = ultimoDia.actividad.turno1.sumBy { it.pasos }
    val acumuladoTurno2 = ultimoDia.actividad.turno2.sumBy { it.pasos }

    // Retorna la suma total de acumulado de ambos turnos
    return acumuladoTurno1 + acumuladoTurno2
}

fun obtenerListaDivisionSemana(semana: List<SemanaItem>): List<Float> {
    val listaDivision = mutableListOf<Float>()

    // Iterar sobre cada objeto SemanaItem
    for (semanaItem in semana) {
        val sumaDia = calcularSumaDia(semanaItem)
        val division = sumaDia / 3000f // Dividir por 3000
        listaDivision.add(division)
    }

    // Asegurarse de que la lista tenga exactamente 5 elementos
    while (listaDivision.size < 5) {
        listaDivision.add(0f) // Agregar 0 para rellenar si hay menos de 5 días
    }

    // Si la lista tiene más de 5 elementos, recortarla a solo 5
    if (listaDivision.size > 5) {
        listaDivision.subList(5, listaDivision.size).clear()
    }

    return listaDivision
}

fun calcularSumaDia(semanaItem: SemanaItem): Float {
    var sumaPasos = 0f

    // Suma los pasos de Turno1Item
    for (turno1Item in semanaItem.actividad.turno1) {
        sumaPasos += turno1Item.pasos.toFloat()
    }

    // Suma los pasos de Turno2Item
    for (turno2Item in semanaItem.actividad.turno2) {
        sumaPasos += turno2Item.pasos.toFloat()
    }
    return sumaPasos
}



@Preview
@Composable
fun Diario(){
    //HomeScreen()
}