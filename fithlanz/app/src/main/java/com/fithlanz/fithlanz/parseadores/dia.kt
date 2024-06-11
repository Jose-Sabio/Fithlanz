package com.fithlanz.fithlanz.parseadores

import com.fithlanz.fithlanz.models.Actividad
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import org.json.JSONArray

fun obtenerListaDias(jsonString: String): List<DiaItem> {
    val gson = Gson()
    val tipoListaDias = object : TypeToken<List<DiaItem>>() {}.type
    return gson.fromJson(jsonString, tipoListaDias)
}

data class DiaItem(
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
    val pasos: Int,
    val segmentos: Int,
    val distancia: Double? // Este campo es opcional ya que no está presente en todos los objetos Turno1
)

data class Turno2Item(
    val tipo: String,
    val tiempo: String,
    val pasos: Int,
    val segmentos: Int,
    val distancia: Double? // Este campo es opcional ya que no está presente en todos los objetos Turno2
)

fun parsearDias(diasJsonArray: JSONArray): List<DiaItem> {
    val listaDias = mutableListOf<DiaItem>()
    for (i in 0 until diasJsonArray.length()) {
        val diaItemJson = diasJsonArray.getJSONObject(i)
        val dia = diaItemJson.getString("dia")
        val actividadJson = diaItemJson.getJSONObject("actividad")
        val actividad = parsearActividad(actividadJson)
        listaDias.add(DiaItem(dia, actividad))
    }
    return listaDias
}