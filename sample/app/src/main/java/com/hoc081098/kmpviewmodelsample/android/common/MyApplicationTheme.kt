@file:Suppress("MagicNumber")

package com.hoc081098.kmpviewmodelsample.android.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal val Blue10 = Color(0xFF001F28)
internal val Blue20 = Color(0xFF003544)
internal val Blue30 = Color(0xFF004D61)
internal val Blue40 = Color(0xFF006780)
internal val Blue80 = Color(0xFF5DD5FC)
internal val Blue90 = Color(0xFFB8EAFF)
internal val Blue95 = Color(0xFFDDF4FF)
internal val DarkGreen10 = Color(0xFF0D1F12)
internal val DarkGreen20 = Color(0xFF223526)
internal val DarkGreen30 = Color(0xFF394B3C)
internal val DarkGreen40 = Color(0xFF4F6352)
internal val DarkGreen80 = Color(0xFFB7CCB8)
internal val DarkGreen90 = Color(0xFFD3E8D3)
internal val DarkGreenGray10 = Color(0xFF1A1C1A)
internal val DarkGreenGray20 = Color(0xFF2F312E)
internal val DarkGreenGray90 = Color(0xFFE2E3DE)
internal val DarkGreenGray95 = Color(0xFFF0F1EC)
internal val DarkGreenGray99 = Color(0xFFFBFDF7)
internal val DarkPurpleGray10 = Color(0xFF201A1B)
internal val DarkPurpleGray20 = Color(0xFF362F30)
internal val DarkPurpleGray90 = Color(0xFFECDFE0)
internal val DarkPurpleGray95 = Color(0xFFFAEEEF)
internal val DarkPurpleGray99 = Color(0xFFFCFCFC)
internal val Green10 = Color(0xFF00210B)
internal val Green20 = Color(0xFF003919)
internal val Green30 = Color(0xFF005227)
internal val Green40 = Color(0xFF006D36)
internal val Green80 = Color(0xFF0EE37C)
internal val Green90 = Color(0xFF5AFF9D)
internal val GreenGray30 = Color(0xFF414941)
internal val GreenGray50 = Color(0xFF727971)
internal val GreenGray60 = Color(0xFF8B938A)
internal val GreenGray80 = Color(0xFFC1C9BF)
internal val GreenGray90 = Color(0xFFDDE5DB)
internal val Orange10 = Color(0xFF380D00)
internal val Orange20 = Color(0xFF5B1A00)
internal val Orange30 = Color(0xFF812800)
internal val Orange40 = Color(0xFFA23F16)
internal val Orange80 = Color(0xFFFFB59B)
internal val Orange90 = Color(0xFFFFDBCF)
internal val Orange95 = Color(0xFFFFEDE8)
internal val Purple10 = Color(0xFF36003C)
internal val Purple20 = Color(0xFF560A5D)
internal val Purple30 = Color(0xFF702776)
internal val Purple40 = Color(0xFF8B418F)
internal val Purple80 = Color(0xFFFFA9FE)
internal val Purple90 = Color(0xFFFFD6FA)
internal val Purple95 = Color(0xFFFFEBFA)
internal val PurpleGray30 = Color(0xFF4D444C)
internal val PurpleGray50 = Color(0xFF7F747C)
internal val PurpleGray60 = Color(0xFF998D96)
internal val PurpleGray80 = Color(0xFFD0C3CC)
internal val PurpleGray90 = Color(0xFFEDDEE8)
internal val Red10 = Color(0xFF410002)
internal val Red20 = Color(0xFF690005)
internal val Red30 = Color(0xFF93000A)
internal val Red40 = Color(0xFFBA1A1A)
internal val Red80 = Color(0xFFFFB4AB)
internal val Red90 = Color(0xFFFFDAD6)
internal val Teal10 = Color(0xFF001F26)
internal val Teal20 = Color(0xFF02363F)
internal val Teal30 = Color(0xFF214D56)
internal val Teal40 = Color(0xFF3A656F)
internal val Teal80 = Color(0xFFA2CED9)
internal val Teal90 = Color(0xFFBEEAF6)

@Suppress("LongMethod")
@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) {
    darkColorScheme(
      primary = Purple80,
      onPrimary = Purple20,
      primaryContainer = Purple30,
      onPrimaryContainer = Purple90,
      secondary = Orange80,
      onSecondary = Orange20,
      secondaryContainer = Orange30,
      onSecondaryContainer = Orange90,
      tertiary = Blue80,
      onTertiary = Blue20,
      tertiaryContainer = Blue30,
      onTertiaryContainer = Blue90,
      error = Red80,
      onError = Red20,
      errorContainer = Red30,
      onErrorContainer = Red90,
      background = DarkPurpleGray10,
      onBackground = DarkPurpleGray90,
      surface = DarkPurpleGray10,
      onSurface = DarkPurpleGray90,
      surfaceVariant = PurpleGray30,
      onSurfaceVariant = PurpleGray80,
      inverseSurface = DarkPurpleGray90,
      inverseOnSurface = DarkPurpleGray10,
      outline = PurpleGray60,
    )
  } else {
    lightColorScheme(
      primary = Purple40,
      onPrimary = Color.White,
      primaryContainer = Purple90,
      onPrimaryContainer = Purple10,
      secondary = Orange40,
      onSecondary = Color.White,
      secondaryContainer = Orange90,
      onSecondaryContainer = Orange10,
      tertiary = Blue40,
      onTertiary = Color.White,
      tertiaryContainer = Blue90,
      onTertiaryContainer = Blue10,
      error = Red40,
      onError = Color.White,
      errorContainer = Red90,
      onErrorContainer = Red10,
      background = DarkPurpleGray99,
      onBackground = DarkPurpleGray10,
      surface = DarkPurpleGray99,
      onSurface = DarkPurpleGray10,
      surfaceVariant = PurpleGray90,
      onSurfaceVariant = PurpleGray30,
      inverseSurface = DarkPurpleGray20,
      inverseOnSurface = DarkPurpleGray95,
      outline = PurpleGray50,
    )
  }
  val typography = Typography(
    bodyLarge = TextStyle(
      fontFamily = FontFamily.Default,
      fontWeight = FontWeight.Normal,
      fontSize = 16.sp,
    ),
  )
  val shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp),
  )

  MaterialTheme(
    colorScheme = colorScheme,
    typography = typography,
    shapes = shapes,
    content = content,
  )
}
