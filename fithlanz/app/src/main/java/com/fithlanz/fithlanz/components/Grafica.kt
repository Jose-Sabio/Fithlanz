import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fithlanz.fithlanz.ui.theme.FithlanzTheme

@Composable
fun LineGraphCard(points: List<Float>, onClick: () -> Unit) {
    val color=MaterialTheme.colorScheme.primary

    val maxValue = points.maxOrNull() ?: 0f
    val minValue = points.minOrNull() ?: 0f
    val range = maxValue - minValue

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surface,shape = RoundedCornerShape(8.dp)),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface,shape = RoundedCornerShape(8.dp)).padding(16.dp)
        ) {
            Text(
                text = "Historial",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface, // Utiliza el color de texto en la superficie del tema Material
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.BottomStart // Alinea la gráfica en la parte inferior de la caja
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    val spacing = width / (points.size - 1)

                    val path = Path().apply {
                        points.forEachIndexed { index, point ->
                            val x = index * spacing
                            val y = height - ((point - minValue) / range) * height
                            if (index == 0) {
                                moveTo(x, y)
                            } else {
                                val previousX = (index - 1) * spacing
                                val previousY = height - ((points[index - 1] - minValue) / range) * height
                                val controlX = previousX + (x - previousX) / 2
                                cubicTo(
                                    controlX, previousY,
                                    controlX, y,
                                    x, y
                                )
                            }
                        }
                        lineTo(width, height)
                        lineTo(0f, height)
                        close()
                    }
                    drawPath(
                        path = path,
                        color = color, // Utiliza el color primario del tema Material
                    )
                }
            }
        }
    }
}
@Composable
@Preview(showBackground = true)
fun LineGraphCardPreview() {
    FithlanzTheme {
        LineGraphCard(
            points = listOf(10f, 20f, 15f, 230f, 125f, 300f, 20f),
            onClick = { println("Graph clicked!") }
        )
    }
}