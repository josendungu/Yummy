package com.example.yummy.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.lang.NullPointerException

data class PositiveAction(
    val positiveBtnText: String,
    val onPositiveAction: () -> Unit,
)

data class NegativeAction(
    val negativeActionText: String,
    val onNegativeAction: () -> Unit
)


//Just use a normal class that gets parameters, all this is unnecessary
//This is how to build a Builder
class GenericDialogInfo private constructor(
    builder: GenericDialogInfo.Builder
){
    private val title: String
    private val onDismiss: () -> Unit
    private val description: String?
    private val positiveAction: PositiveAction?
    private val negativeAction: NegativeAction?

    init {
        if (builder.title == null){
            throw NullPointerException("GenericDialogInfo title cannot be null")
        }

        if (builder.onDismiss == null){
            throw NullPointerException("GenericDialogInfo onDismiss function cannot be null")
        }

        this.title = builder.title!!
        this.onDismiss = builder.onDismiss!!
        this.description = builder.description
        this.positiveAction = builder.positiveAction
        this.negativeAction = builder.negativeAction
    }

    class Builder{

        //setting parameters as private but getting as public
        var title: String? = null
            private set

        var onDismiss: (() -> Unit)?= null
            private set

        var description: String? = null
            private set

        var positiveAction: PositiveAction? = null
            private set

        var negativeAction: NegativeAction? = null
            private set


        fun title(title: String): Builder{
            this.title = title
            return this
        }

        fun description(description: String): Builder {
            this.description = description
            return this
        }

        fun onDismiss(onDismiss: () -> Unit): Builder {
            this.onDismiss = onDismiss
            return this
        }

        fun positiveAction(positiveAction: PositiveAction): Builder {
            this.positiveAction = positiveAction
            return this
        }

        fun negativeAction(negativeAction: NegativeAction): Builder {
            this.negativeAction = negativeAction
            return this
        }

        fun build() = GenericDialogInfo(this)

    }
}


@Composable
fun GenericDialog(
    modifier: Modifier,
    onDismiss: () -> Unit,
    title: String,
    description: String? = null,
    positiveAction: PositiveAction? = null,
    negativeAction: NegativeAction? = null
){

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        title = { Text(text = title) },
        text = {
            if (description != null){
                Text(text = description)
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(7.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (negativeAction != null){
                    Button(
                        onClick = negativeAction.onNegativeAction,
                        modifier = Modifier.padding(7.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.onError
                        )
                    ) {
                        Text(text = negativeAction.negativeActionText)
                    }
                }

                if (positiveAction != null){
                    Button(
                        onClick = positiveAction.onPositiveAction,
                        modifier = Modifier.padding(7.dp),

                    ) {
                        Text(text = positiveAction.positiveBtnText)
                    }
                }
            }
        }

    )

}
