package com.edurda77.data.repository

import com.edurda77.data.handler.handleResponse
import com.edurda77.data.mapper.toMeasurementUnit
import com.edurda77.data.remote.newDtos.measurement.MeasurementUnitDto
import com.edurda77.domain.model.newModels.MeasurementUnit
import com.edurda77.domain.repository.MeasurementUnitRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.NEW_BASE_URL
import com.edurda77.domain.utils.ResultWork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MeasurementUnitRepositoryImpl(
    private val httpClient: HttpClient
) : MeasurementUnitRepository {

    override suspend fun getUnits(
        accessToken: String,
    ): ResultWork<List<MeasurementUnit>, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.get(NEW_BASE_URL + "measurement-units") {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                }
                result.call.body<List<MeasurementUnitDto>>().map { it.toMeasurementUnit() }
            }
        }
    }
}