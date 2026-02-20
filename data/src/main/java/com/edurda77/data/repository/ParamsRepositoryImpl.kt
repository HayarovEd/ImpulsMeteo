package com.edurda77.data.repository

import com.edurda77.data.handler.handleResponse
import com.edurda77.data.mapper.toParam
import com.edurda77.data.mapper.toParamDto
import com.edurda77.data.remote.newDtos.param.ParamDto
import com.edurda77.domain.model.newModels.History
import com.edurda77.domain.model.newModels.Param
import com.edurda77.domain.repository.ParamsRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.FROM_DATE_PARAMETER
import com.edurda77.domain.utils.LIMIT_PARAMETER
import com.edurda77.domain.utils.NEW_BASE_URL
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.TO_DATE_PARAMETER
import com.edurda77.domain.utils.convertToLocalDateTime
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class ParamsRepositoryImpl(
    private val httpClient: HttpClient
) : ParamsRepository {

    override suspend fun getHistoryDeviceById(
        accessToken: String,
        deviceId: String,
        fromDate: String,
        toDate: String,
        limit: Int,
    ): ResultWork<Map<String, List<History>>, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val jsonText =
                    httpClient.get(NEW_BASE_URL + "device-params/" + deviceId + "/params") {
                        contentType(ContentType.Application.Json)
                        bearerAuth(accessToken)
                        url {
                            parameter(FROM_DATE_PARAMETER, fromDate)
                            parameter(TO_DATE_PARAMETER, toDate)
                            parameter(LIMIT_PARAMETER, limit)
                        }
                    }.bodyAsText()
                val jsonArray = Json.parseToJsonElement(jsonText).jsonArray
                val result = mutableMapOf<String, MutableList<History>>()

                jsonArray.forEach { jsonElement ->
                    val jsonObject = jsonElement.jsonObject

                    val time = jsonObject["time"]?.jsonPrimitive?.content ?: return@forEach

                    jsonObject.forEach { (key, value) ->
                        if (key != "time") {
                            val doubleValue = value.jsonPrimitive.double
                            result.getOrPut(key) { mutableListOf() }
                                .add(
                                    History(
                                        time = convertToLocalDateTime(time),
                                        value = doubleValue
                                    )
                                )
                        }
                    }
                }
                result.mapValues { (_, histories) ->
                    histories.sortedBy { it.time }
                }
            }
        }
    }

    override suspend fun updateParam(
        accessToken: String,
        param: Param,
    ): ResultWork<Param, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result =
                    httpClient.patch(NEW_BASE_URL + "device-params/" + param.id) {
                        contentType(ContentType.Application.Json)
                        bearerAuth(accessToken)
                        setBody(
                            param.toParamDto()
                        )
                    }
                result.call.body<ParamDto>().toParam()
            }
        }
    }
}