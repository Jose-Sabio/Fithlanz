import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

import androidx.compose.ui.text.input.ImeAction

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fithlanz.fithlanz.Auth.AuthManager
import com.fithlanz.fithlanz.models.Usuario
import com.fithlanz.fithlanz.navigation.AppScreens
import com.fithlanz.fithlanz.ui.theme.fithlanztheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun Registro(
    context: Context,
    authManager: AuthManager,
    navController: NavController,
    refreshToken: MutableState<String>,
    usuario: Usuario?,
    condicion: Boolean
) {
    println(usuario)
    if(usuario!=null){
        println("el usuario no es nulo")
        updateRefreshToken(
            context,
            usuario.id,
            refreshToken.value,
            { navController.navigate(AppScreens.SecondScreen.route) },
            {},
            navController
        )
    }else{
        authManager.Usuario(context)?.email?.let { getUserIdByEmail(it,context,{
respuesta->
            if(respuesta!=null){
                respuesta?.let { it1 -> updateRefreshToken(context, it1,refreshToken.value,{

                },{},navController) }
            }else{

            }

        }) }
    }
    val scrollState = rememberScrollState()
    var selectedCentro by remember { mutableStateOf("") }
    var selectedTurno by remember { mutableStateOf("") }
    var alias by remember { mutableStateOf("") }
    var expandedCentro by remember { mutableStateOf(false) }
    var expandedTurno by remember { mutableStateOf(false) }
    val centros = remember { mutableStateListOf<String>() }
    val turnos = listOf("Mañana", "Tarde")
    val isButtonEnabled = selectedCentro.isNotEmpty() && selectedTurno.isNotEmpty() && alias.isNotEmpty()

    LaunchedEffect(Unit) {
        fetchCentros(context) { result ->
            centros.clear()
            centros.addAll(result)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Antes de empezar tenemos que configurar algunas cosas",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Centro",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        ExposedDropdownMenuBox(
            expanded = expandedCentro,
            onExpandedChange = { expandedCentro = !expandedCentro }
        ) {
            TextField(
                value = selectedCentro,
                onValueChange = { selectedCentro = it },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCentro)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                )
            )
            ExposedDropdownMenu(
                expanded = expandedCentro,
                onDismissRequest = { expandedCentro = false }
            ) {
                centros.forEach { centro ->
                    DropdownMenuItem(
                        text = { Text(text = centro) },
                        onClick = {
                            selectedCentro = centro
                            expandedCentro = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Turno",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        ExposedDropdownMenuBox(
            expanded = expandedTurno,
            onExpandedChange = { expandedTurno = !expandedTurno }
        ) {
            TextField(
                value = selectedTurno,
                onValueChange = { selectedTurno = it },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTurno)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                )
            )
            ExposedDropdownMenu(
                expanded = expandedTurno,
                onDismissRequest = { expandedTurno = false }
            ) {
                turnos.forEach { turno ->
                    DropdownMenuItem(
                        text = { Text(text = turno) },
                        onClick = {
                            selectedTurno = turno
                            expandedTurno = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Alias input
        Text(
            text = "Alias",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        TextField(
            value = alias,
            onValueChange = { alias = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            placeholder = { Text("Introduce un alias") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.background,
            )
        )

        Spacer(modifier = Modifier.height(90.dp))
        // Button
        Button(
            onClick = {if(usuario==null){
                authManager.Usuario(context)?.email?.let {
                    createUser(
                        context, it,
                        authManager.obtenerautenticacion().uid,
                        alias,
                        selectedCentro,
                        0,
                        if (selectedTurno == "Mañana") "M" else "T",
                        authManager.Usuario(context)!!.photoUrl.toString(),
                        0,
                        "",
                        refreshToken.value,
                        navController
                    )
                }
            }
            },
            enabled = isButtonEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isButtonEnabled) fithlanztheme.primary else fithlanztheme.onSecondary,
                contentColor = if (isButtonEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Continuar")
        }
    }
}


