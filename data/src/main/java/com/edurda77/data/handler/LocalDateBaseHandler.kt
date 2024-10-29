package com.edurda77.data.handler

import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

suspend fun <D> handleWriteToDataBase(data: suspend () -> D): ResultWork<D, DataError.LocalDateBase> {
    return try {
        ResultWork.Success(data())

    } catch (e: Exception) {
        e.printStackTrace()
        ResultWork.Error(DataError.LocalDateBase.ERROR_WRITE_DATA)
    }
}

fun <D> handleReadFromDataBase(data: () -> Flow<D>): Flow<ResultWork<D, DataError.LocalDateBase>> {
    return flow<ResultWork<D, DataError.LocalDateBase>> {
        data.invoke().collect { collector ->
            emit(
                ResultWork.Success(collector)
            )
        }
    }.catch {
        emit(
            ResultWork.Error(DataError.LocalDateBase.ERROR_READ_DATA)
        )
    }
}