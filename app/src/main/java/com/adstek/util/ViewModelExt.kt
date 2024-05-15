package com.adstek.util

import androidx.lifecycle.MutableLiveData
import com.adstek.util.network.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


fun <T> BaseViewModel.emitFlowResults(
    liveDataObject: MutableLiveData<DataState<T>>,
    networkRequest: () -> Flow<DataState<T>>
) {
    coroutineScope.launch(Dispatchers.IO) {
        networkRequest()
            .onStart { liveDataObject.postValue(DataState.Loading) }
            .onEach {
                liveDataObject.postValue(it)
            }
            .catch {
                liveDataObject.postValue(DataState.Error(Exception(it.localizedMessage)))
            }
            .launchIn(this)

    }
}

fun <T> BaseViewModel.emitFlowResultsToEvent(
    liveDataObject: MutableLiveData<Event<DataState<T>>>,
    networkRequest: () -> Flow<DataState<T>>
) {
    coroutineScope.launch(Dispatchers.IO) {
        networkRequest()
            .onStart { liveDataObject.postValue(Event(DataState.Loading)) }
            .onEach {
                liveDataObject.postValue(Event(it))
            }
            .catch {
                liveDataObject.postValue(Event(DataState.Error(Exception(it.localizedMessage))))
            }
            .launchIn(this)

    }
}