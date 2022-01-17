package com.example.yummy.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.yummy.presentation.components.ConnectivityMonitor
import com.example.yummy.presentation.components.GenericDialog
import com.example.yummy.presentation.components.GenericDialogInfo
import java.util.*

private val DarkColorPalette = darkColors(
    primary = PrimaryDark,
    primaryVariant = PrimaryVariant,
    secondary = Secondary,
    background = DarkGrey,
    onPrimary = onSecondary
)

private val LightColorPalette = lightColors(
    primary = Primary,
    primaryVariant = PrimaryVariant,
    secondary = Secondary,
    background = Color.White,
    onPrimary = onPrimary
)

@Composable
fun YummyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dialogQueue: Queue<GenericDialogInfo>? = null,
    isNetworkAvailable: Boolean = true,
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes
    ){
        Column {
            ConnectivityMonitor(isNetworkAvailable = isNetworkAvailable)
            content()
        }
    }
    if (dialogQueue != null) {
        ProcessDialogQueue(dialogQueue = dialogQueue)
    }
}


@Composable
fun ProcessDialogQueue(
    dialogQueue: Queue<GenericDialogInfo>
){
    dialogQueue.peek()?.let {
        GenericDialog(
            onDismiss = { it.onDismiss },
            title = it.title,
            description = it.description,
            positiveAction = it.positiveAction,
            negativeAction = it.negativeAction
        )
    }
}