package com.example.yummy.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.example.yummy.common.util.DialogQueue
import com.example.yummy.presentation.components.ConnectivityMonitor
import com.example.yummy.presentation.components.GenericDialog
import com.example.yummy.presentation.components.GenericDialogInfo
import java.util.*

private val DarkColorPalette = darkColors(
    primary = Red500,
    primaryVariant = Red200,
    secondary = White500,
    background = DarkGrey
)

private val LightColorPalette = lightColors(
    primary = Red500,
    primaryVariant = Red200,
    secondary = White500,
    background = White700


)

@Composable
fun YummyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dialogQueue: Queue<GenericDialogInfo>? = null,
    isNetworkAvailable: Boolean,
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