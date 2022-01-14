package com.example.yummy.presentation.util

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import com.example.yummy.YummyApplication
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectivityManager @Inject constructor(
    application: YummyApplication
) {
    private val connectionLiveData = ConnectionLiveData(application)
    // observe this in ui
    val isNetworkAvailable = mutableStateOf(false)

    fun registerConnectionObserver(lifecycleOwner: LifecycleOwner){
        connectionLiveData.observe(lifecycleOwner, { isConnected ->
            isConnected?.let { isNetworkAvailable.value = it }
        })
    }

    fun unregisterConnectionObserver(lifecycleOwner: LifecycleOwner){
        connectionLiveData.removeObservers(lifecycleOwner)
    }
}