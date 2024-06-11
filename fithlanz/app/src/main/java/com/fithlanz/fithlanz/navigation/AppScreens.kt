package com.fithlanz.fithlanz.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppScreens(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null) {

    object FirstScreen: AppScreens(
        route = "first_screen",
    )

    object SecondScreen: AppScreens(
        route = "second_screen"
    )

    object Diario : AppScreens(
        route = "diario_screen",
        title = "Diario",
        icon = Icons.Filled.Event
    )

    object Retos : AppScreens(
        route = "Retos_screen",
        title = "Logros",
        icon = Icons.Filled.Star
    )
    object Historial : AppScreens(
        route = "Historial_screen",
    )
    object Configuracion : AppScreens(
        route = "Configuracion_screen",
    )

    object Wifi:AppScreens(
        route = "Wifi_screen"
    )

    object Perfil : AppScreens(
        route = "Perfil_screen",
        title = "Perfil",
        icon = Icons.Outlined.Face
    )
    object Comunidad : AppScreens(
        route = "Comunidad_screen",
        title = "Comunidad",
        icon = Icons.Filled.Group
    )
    object Registro : AppScreens(
        route = "Registro",
        title = "Perfil",
    )
 }