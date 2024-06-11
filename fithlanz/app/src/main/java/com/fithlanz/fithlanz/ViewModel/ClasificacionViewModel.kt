package com.fithlanz.fithlanz.ViewModel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fithlanz.fithlanz.models.Usuario
import kotlinx.coroutines.launch
import obtenerclasificacion

class ClasificacionViewModel : ViewModel() {
    private val _clasificacion = mutableStateOf<List<Usuario>>(emptyList())
    val clasi: State<List<Usuario>> = _clasificacion

    fun obtenerClasificacion(context: Context) {
        viewModelScope.launch {
            obtenerclasificacion(context) { listaClasificacion ->
                _clasificacion.value = listaClasificacion
            }
        }
    }
}
