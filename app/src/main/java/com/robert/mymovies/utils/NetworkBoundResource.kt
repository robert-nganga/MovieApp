package com.robert.mymovies.utils

import android.util.Log
import kotlinx.coroutines.flow.*

inline fun <ResultType, RequestType>networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Resource.loading(data))
        try {
            saveFetchResult(fetch())
            query().map { Resource.success(it) }
        } catch (throwable: Throwable) {
            Log.e("NetworkBoundResource", "An error happened: $throwable", throwable)
            query().map { Resource.error(throwable.message ?: "Unknown error", it) }
        }
    } else {
        query().map { Resource.success(it) }
    }

    emitAll(flow)
}
