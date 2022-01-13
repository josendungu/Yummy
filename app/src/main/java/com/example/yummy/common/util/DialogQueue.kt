package com.example.yummy.common.util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.yummy.presentation.components.GenericDialogInfo
import com.example.yummy.presentation.components.PositiveAction
import java.util.*

class DialogQueue{

    val queue: MutableState<Queue<GenericDialogInfo>> = mutableStateOf(LinkedList())

    private fun removeHeadMessage(){
        if (queue.value.isNotEmpty()){
            val  update = queue.value
            update.remove()
            queue.value = LinkedList()//force recomposition
            queue.value = update
        }
    }

    fun appendErrorMessage(title: String, description: String){
        queue.value.offer(
            GenericDialogInfo.Builder()
                .title(title)
                .onDismiss(this::removeHeadMessage)
                .description(description)
                .positiveAction(PositiveAction(
                    positiveBtnText = "OK",
                    onPositiveAction = this::removeHeadMessage
                ))
                .build()
        )
    }
}