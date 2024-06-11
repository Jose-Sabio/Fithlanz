package com.fithlanz.fithlanz.models


// Los logros se leen desde el cliente por lo que no hay metodos de crud si no que estos se llenaran conforme hagamos la lectura masiva.
data class Logro(
    val descr: String,
    val goal: Int,
    val title: String,
    val type: String,
    val nivel:String
)