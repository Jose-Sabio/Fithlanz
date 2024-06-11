import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.squareup.picasso.Picasso
import java.lang.Exception

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CircularImageWithPicasso(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    circleSize: Dp
) {
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(imageUrl) {
        loadImage(imageUrl) { loadedBitmap ->
            imageBitmap = loadedBitmap
        }
    }

    Box(
        modifier = modifier
            .size(circleSize)
            .then(Modifier.background(Color.Gray, shape = CircleShape))
    ) {
        imageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = Modifier
                    .size(circleSize)
                    .clip(CircleShape)
                    .align(Alignment.Center)
            )
        }
    }
}

private suspend fun loadImage(url: String, onSuccess: (Bitmap) -> Unit) {
    withContext(Dispatchers.IO) {
        try {
            val bitmap = Picasso.get().load(url).get()
            onSuccess(bitmap)
        } catch (e: Exception) {
            // Handle error loading image
        }
    }
}


@Preview
@Composable
fun YourComposable() {
    CircularImageWithPicasso(
        imageUrl = "https://lh3.googleusercontent.com/a/ACg8ocJ8W5daiC9f71jeE7D40LSZZgFmFi6c5E-sG74KRddCnQ3Cng=s96-c",
        contentDescription = "Descripción de la imagen",
        circleSize = 200.dp
    )
}

