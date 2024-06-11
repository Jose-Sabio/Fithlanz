package com.fithlanz.fithlanz.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fithlanz.fithlanz.R


private val inriasans= FontFamily(
    Font(R.font.inriasansregular),
            Font(R.font.inriasans_bold),
    Font(R.font.inriasansitalic)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = inriasans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),


    titleLarge = TextStyle(
        fontFamily = inriasans,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
/* Other default text styles to override
labelSmall = TextStyle(
fontFamily = FontFamily.Default,
fontWeight = FontWeight.Medium,
fontSize = 11.sp,
lineHeight = 16.sp,
letterSpacing = 0.5.sp
)
*/
)