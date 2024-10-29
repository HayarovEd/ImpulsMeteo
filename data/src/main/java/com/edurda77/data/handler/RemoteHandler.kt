package com.edurda77.data.handler


import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.serialization.SerializationException
import java.net.UnknownHostException

suspend fun <D> handleResponse(data: suspend () -> D): ResultWork<D, DataError> {
    return try {
        ResultWork.Success(data())
    } catch (e: ClientRequestException) {
        when (e.response.status.value) {
            400 -> ResultWork.Error(DataError.Network.BAD_REQUEST)
            401 -> ResultWork.Error(DataError.Network.UNAUTHORIZED)
            404 -> ResultWork.Error(DataError.Network.NOT_FOUND)
            else -> ResultWork.Error(DataError.Network.UNKNOWN)
        }
    } catch (e: ServerResponseException) {
        e.printStackTrace()
        ResultWork.Error(DataError.Network.SERVER_ERROR)
    } catch (e: UnknownHostException) {
        e.printStackTrace()
        ResultWork.Error(DataError.Network.NO_INTERNET)
    }  catch (e: SerializationException) {
        e.printStackTrace()
        ResultWork.Error(DataError.SerializationError.FORMAT_ERROR)
    } catch (e: Exception) {
        e.printStackTrace()
        ResultWork.Error(DataError.Network.UNKNOWN)
    }
}
