package com.fithlanz.fithlanz.components

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.fithlanz.fithlanz.R
import com.fithlanz.fithlanz.models.Actividades
import com.fithlanz.fithlanz.models.Logro

@Composable
fun CardWithProgressBar(logro: Logro, progress: Float, tipo: Actividades, context:Context) {
    val showDialog = remember { mutableStateOf(false) }
    val condition=progress >= 1
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { showDialog.value=true },
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                var painter: Painter? = null
                if (logro.type.equals( Actividades.Tiempo.toString())) {
                    if(logro.nivel.equals("Principiante")){
                        painter = painterResource(id = R.drawable.reto_bronce)
                    }else if(logro.nivel.equals("Intermedio")){
                        painter = painterResource(id = R.drawable.reto_plata)
                    }else{
                        painter = painterResource(id = R.drawable.reto)
                    }
                } else {
                    if(logro.nivel.equals("Principiante")){
                        painter = painterResource(id = R.drawable.reto2_bronce)
                    }else if(logro.nivel.equals("Intermedio")){
                        painter = painterResource(id = R.drawable.reto2_plata)
                    }else{
                        painter = painterResource(id = R.drawable.reto2)
                    }
                }
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(125.dp)
                        .padding(15.dp)
                )
                Text(
                    text = logro.title,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                    , modifier = Modifier.padding(16.dp)
                )
            }
            ProgressBar(progress = progress)
        }
    }
    if (showDialog.value) {
        CustomDialog(logro,onDismiss = { showDialog.value = false },tipo,condition,context)
    }
}

@Composable
fun ProgressBar(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
            .background(MaterialTheme.colorScheme.inverseOnSurface, RoundedCornerShape(8.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .height(16.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                )
        )
    }
}

@Composable
fun CustomDialog(logro: Logro, onDismiss: () -> Unit, tipo: Actividades, isButtonHighlighted: Boolean, context: Context) {
    val imageResource =if (logro.type.equals( Actividades.Tiempo.toString())) {
        if(logro.nivel.equals("Principiante")){
            R.drawable.reto_bronce
        }else if(logro.nivel.equals("Intermedio")){
            R.drawable.reto_plata
        }else{
            R.drawable.reto
        }
    } else {
        if(logro.nivel.equals("Principiante")){
            R.drawable.reto2_bronce
        }else if(logro.nivel.equals("Intermedio")){
            R.drawable.reto2_plata
        }else{
            R.drawable.reto2
        }
    }

    val configuration = LocalConfiguration.current
    val isVertical = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    if (isVertical) {
        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(dismissOnClickOutside = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(700.dp)
                    .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),  // Ensure some horizontal padding for the dialog content
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = imageResource),
                        contentDescription = null,
                        modifier = Modifier.size(300.dp)
                    )
                    Text(
                        text = logro.title,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = logro.descr,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(82.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                if (isButtonHighlighted) {
                                    val sendIntent: Intent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, "He conseguido el logro ${logro.title} en Fitlanz https://www.ieshlanz.es/")
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, null)
                                    context.startActivity(shareIntent)                                } else {
                                    val toast = Toast.makeText(context, "Solo podrás compartir cuando lo hayas completado", Toast.LENGTH_LONG)
                                    toast.show()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            contentPadding = PaddingValues(12.dp),
                            colors = ButtonDefaults.buttonColors(if (isButtonHighlighted) {
                                MaterialTheme.colorScheme.secondary
                            } else {
                                MaterialTheme.colorScheme.onSecondary
                            })
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Compartir",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Compartir")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onDismiss() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            contentPadding = PaddingValues(12.dp),
                        ) {
                            Text("Volver")
                        }
                    }
                }
            }
        }
    }
}

