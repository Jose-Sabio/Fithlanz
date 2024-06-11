package com.fithlanz.fithlanz.ViewModel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fithlanz.fithlanz.models.Logro
import kotlinx.coroutines.launch
import obtenerLogros

class LogrosViewModel : ViewModel() {
    private val _logros = mutableStateOf<List<Logro>>(emptyList())
    val logros: State<List<Logro>> = _logros

    fun obtenerLogros(context: Context) {
        viewModelScope.launch {
            obtenerLogros(context) { listaLogro ->
                _logros.value = listaLogro
            }
        }
    }
}

