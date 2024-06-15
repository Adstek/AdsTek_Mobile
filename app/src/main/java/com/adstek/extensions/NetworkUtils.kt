package com.adstek.extensions

import com.adstek.data.remote.response.DataState
import com.adstek.data.remote.response.ErrorResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.UnknownHostException


//fun <T> makeNetworkRequest(dispatcher: CoroutineDispatcher = Dispatchers.IO,
//                      request: suspend () -> Response<T>
//): Flow<Response<T>> {
//    return flow {
//        try {
//            emit(DataState.Loading)
//            val apiResponse = request.invoke()
//            emit(DataState.Success(apiResponse.body()))
//
//        } catch (e: HttpException) {
//            val body = e.response()?.errorBody()
////            val message = e.response()?.message()
//            val rawResponse = e.response()?.raw()
//            val code = e.response()?.code()
//            val gson = GsonBuilder().setLenient().create()
//
//
//        }
//        catch (e: UnknownHostException){
//            Timber.tag("requestException").e("$e")
//            emit(DataState.Error(Exception("Please check your internet connection and try again")))
//        }
//        catch (e: IOException) {
//            Timber.tag("requestException").e("$e")
//            emit(DataState.Error(Exception("Check your internet and try again.")))
//        }
//        catch (e: JsonSyntaxException) {
//            val errorMessage = "Timber: ${request.javaClass.enclosingMethod?.name} --> ${e.javaClass} --> \n ${e.localizedMessage}"
//            Timber.tag("requestException").e(errorMessage)
//
//            emit(DataState.Error(Exception(SOMETHING_WENT_WRONG)))
//        }
//    }.flowOn(dispatcher)
//}

fun <T> makeNetworkRequest(dispatcher: CoroutineDispatcher = Dispatchers.IO,
                           request: suspend () -> Response<T>
): Flow<DataState<T>> {
    return flow {
        emit(DataState.Loading)
        try {
            val response = request.invoke()
            if (response.isSuccessful) {
                emit(DataState.Success(response.body()))
            } else {
                val errorResponse: ErrorResponse? = try {
                    response.errorBody()?.string()?.let {
                        Gson().fromJson(it, ErrorResponse::class.java)
                    }
                } catch (e: JsonSyntaxException) {
                    null
                }

                val errorMessage = errorResponse?.message?.values?.flatten()?.joinToString("\n")
                    ?: response.message()
                emit(DataState.Error(Exception(errorMessage)))
            }
        }
        catch (e: UnknownHostException){
            Timber.tag("requestException").e("$e")
            emit(DataState.Error(Exception("Please check your internet connection and try again")))
        }
        catch (e: IOException) {
            Timber.tag("requestException").e("$e")
            emit(DataState.Error(Exception("Check your internet and try again.")))
        }
        catch (e: JsonSyntaxException) {
            val errorMessage = "Timber: ${request.javaClass.enclosingMethod?.name} --> ${e.javaClass} --> \n ${e.localizedMessage}"
            Timber.tag("requestException").e(errorMessage)

        }
    }.flowOn(dispatcher)
}