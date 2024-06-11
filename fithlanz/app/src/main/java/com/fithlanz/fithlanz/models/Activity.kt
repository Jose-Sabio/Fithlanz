package com.fithlanz.fithlanz.models
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

import java.util.Date

// Las Actividades se leen desde el cliente por lo que no hay metodos de crud si no que estos se llenaran conforme hagamos la lectura masiva.

enum class Actividades {
    A_PIE, BICICLETA, Tiempo, Contador
}

fun obtenerListaSemana(jsonString: String): List<SemanaItem> {
    val gson = Gson()
    val tipoListaSemana = object : TypeToken<List<SemanaItem>>() {}.type
    return gson.fromJson(jsonString, tipoListaSemana)
}

data class SemanaItem(
    val dia: String,
    val actividad: Actividad
)

data class Actividad(
    val turno1: List<Turno1Item>,
    val turno2: List<Turno2Item>
)

data class Turno1Item(
    val tipo: String,
    val tiempo: String,
    val segmentos: Int,
    val pasos: Int,
    val distancia: Double? // Este campo es opcional ya que no está presente en todos los objetos Turno1
)

data class Turno2Item(
    val tipo: String,
    val tiempo: String,
    val segmentos: Int,
    val pasos: Int,
    val distancia: Double? // Este campo es opcional ya que no está presente en todos los objetos Turno2
)
