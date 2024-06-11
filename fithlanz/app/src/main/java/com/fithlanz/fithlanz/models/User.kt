package com.fithlanz.fithlanz.models

import com.fithlanz.fithlanz.parseadores.DiaItem

// Los Usuarios se leen desde el cliente por lo que no hay metodos de crud si no que estos se llenaran conforme hagamos la lectura masiva.

class Usuario(
    var id: String,
    var username: String,
    var profileImg: String,
    var acumulado: Int,
    var refreshtoken:String,
    var diario:List<SemanaItem>,
    var semana: List<DiaItem>,
    )





