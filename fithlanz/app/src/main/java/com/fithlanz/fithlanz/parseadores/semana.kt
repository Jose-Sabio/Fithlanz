package com.fithlanz.fithlanz.parseadores

import com.fithlanz.fithlanz.models.Actividad
import com.fithlanz.fithlanz.models.SemanaItem
import com.fithlanz.fithlanz.models.Turno1Item
import com.fithlanz.fithlanz.models.Turno2Item
import org.json.JSONArray
import org.json.JSONObject

fun parsearSemana(semanaJsonArray: JSONArray): List<SemanaItem> {
    val listaSemana = mutableListOf<SemanaItem>()
    for (i in 0 until semanaJsonArray.length()) {
        val semanaItemJson = semanaJsonArray.getJSONObject(i)
        val dia = semanaItemJson.getString("dia")
        val actividadJson = semanaItemJson.getJSONObject("actividad")
        val actividad = parsearActividad(actividadJson)
        listaSemana.add(SemanaItem(dia, actividad))
    }
    return listaSemana
}

fun parsearActividad(actividadJson: JSONObject): Actividad {
    val turno1JsonArray = actividadJson.getJSONArray("Turno1")
    val turno1 = parsearTurno1(turno1JsonArray)

    val turno2JsonArray = actividadJson.getJSONArray("Turno2")
    val turno2 = parsearTurno2(turno2JsonArray)

    return Actividad(turno1, turno2)
}

fun parsearTurno1(turno1JsonArray: JSONArray): List<Turno1Item> {
    val listaTurno1 = mutableListOf<Turno1Item>()
    for (i in 0 until turno1JsonArray.length()) {
        val turno1ItemJson = turno1JsonArray.getJSONObject(i)
        val tipo = turno1ItemJson.getString("tipo")
        val tiempo = turno1ItemJson.getString("tiempo")
        val segmentos = turno1ItemJson.getInt("segmentos")
        val pasos = turno1ItemJson.getInt("pasos")
        val distancia = if (turno1ItemJson.has("distancia")) turno1ItemJson.getDouble("distancia") else null
        listaTurno1.add(Turno1Item(tipo, tiempo, segmentos, pasos, distancia))
    }
    return listaTurno1
}

fun parsearTurno2(turno2JsonArray: JSONArray): List<Turno2Item> {
    val listaTurno2 = mutableListOf<Turno2Item>()
    for (i in 0 until turno2JsonArray.length()) {
        val turno2ItemJson = turno2JsonArray.getJSONObject(i)
        val tipo = turno2ItemJson.getString("tipo")
        val tiempo = turno2ItemJson.getString("tiempo")
        val segmentos = turno2ItemJson.getInt("segmentos")
        val pasos = turno2ItemJson.getInt("pasos")
        val distancia = if (turno2ItemJson.has("distancia")) turno2ItemJson.getDouble("distancia") else null
        listaTurno2.add(Turno2Item(tipo, tiempo, segmentos, pasos, distancia))
    }
    return listaTurno2
}