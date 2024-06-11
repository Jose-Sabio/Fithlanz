package com.fithlanz.fithlanz.components


import CircularImageWithPicasso
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ClassificationRow(
 rank: String,
 userName: String,
 imageUrl: String,
 modifier: Modifier = Modifier,
 contentDescription: String? = null,
 circleSize: Dp = 40.dp // Tamaño ajustado
) {
 Row(
  verticalAlignment = Alignment.CenterVertically,
  horizontalArrangement = Arrangement.Start,
  modifier = modifier
   .padding(horizontal = 16.dp, vertical = 8.dp) // Margen alrededor del componente
   .fillMaxWidth()
   .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp)) // Fondo y bordes redondeados con el color de superficie del tema
   .padding(8.dp) // Padding interno
 ) {
  Spacer(modifier = Modifier.width(15.dp)) // Espacio inicial
  Text(
   text = rank,
   style = MaterialTheme.typography.titleMedium,
   color = MaterialTheme.colorScheme.onSurface,// Utiliza el estilo de texto del tema
   modifier = Modifier.weight(0.5f)
  )
  Spacer(modifier = Modifier.width(10.dp)) // Espacio entre número y nombre
  Text(
   text = userName,
   style = MaterialTheme.typography.titleSmall,
   color = MaterialTheme.colorScheme.onSurface,// Utiliza el estilo de texto del tema
   modifier = Modifier.weight(2f)
  )
  Spacer(modifier = Modifier.width(8.dp)) // Espacio entre nombre e imagen
  CircularImageWithPicasso(
   imageUrl = imageUrl,
   contentDescription = contentDescription,
   circleSize = circleSize
  )
  Spacer(modifier = Modifier.width(15.dp)) // Espacio final
 }
}

@Preview(showBackground = true)
@Composable
fun PreviewClassificationRow() {
 Column {
  ClassificationRow(
   rank = "1",
   userName = "Usuario Ejemplo",
   imageUrl = "https://lh3.googleusercontent.com/a/ACg8ocJ8W5daiC9f71jeE7D40LSZZgFmFi6c5E-sG74KRddCnQ3Cng=s96-c",
   contentDescription = "Imagen de perfil"
  )
  ClassificationRow(
   rank = "1",
   userName = "Usuario Ejemplo",
   imageUrl = "https://lh3.googleusercontent.com/a/ACg8ocJ8W5daiC9f71jeE7D40LSZZgFmFi6c5E-sG74KRddCnQ3Cng=s96-c",
   contentDescription = "Imagen de perfil"
  )
 }

}