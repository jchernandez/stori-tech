package com.android.rojox.core.model

import com.android.rojox.core.utils.CoreException

class DataState<T>(val data: T? = null,
                   val error: DataError? = null,
                   var status: DataStatus = DataStatus.LOADING
)  {

    companion object {
        fun <T> success(data: T): DataState<T> =
            DataState(data = data, status = DataStatus.SUCCESS)
        suspend fun <T> error(exception: java.lang.Exception): DataState<T> =
            DataState(error = DataError(exception.message?:"Not defined error"), status = DataStatus.ERROR)
        suspend fun <T> error(exception: CoreException): DataState<T> =
            DataState(error = DataError(exception.message!!), status = DataStatus.ERROR)
    }

    override fun toString(): String {
       return "Status: ${status.name}, Data: ${data?.toString()}, Error: ${error?.message}"
    }

}

enum class DataStatus {
    LOADING,
    SUCCESS,
    ERROR
}