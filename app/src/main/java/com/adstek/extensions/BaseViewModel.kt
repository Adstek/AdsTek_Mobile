package com.adstek.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.plus
import timber.log.Timber

open class BaseViewModel: ViewModel() {

    // coroutineExceptionHandler to avoid JobCancellationException -/"unCaught coroutine exceptions
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        // 1. Trigger event to prompt error dialog
        // 2. Log to tracking system for observability
        Timber.tag("coroutineExceptionHandler").e("coroutineExceptionHandler --> $throwable")
    }
    private val job = SupervisorJob()
    private val context = Dispatchers.Main + job + exceptionHandler

//    val coroutineScope = CoroutineScope(context)
    val coroutineScope = (viewModelScope + exceptionHandler)

}