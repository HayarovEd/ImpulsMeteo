package com.edurda77.data.repository

import com.edurda77.data.handler.handleResponse
import com.edurda77.data.mapper.toMeasurementUnit
import com.edurda77.data.mapper.toMeasurementUnitDto
import com.edurda77.data.remote.newDtos.measurement.MeasurementUnitCreateRequest
import com.edurda77.data.remote.newDtos.measurement.MeasurementUnitDto
import com.edurda77.domain.model.newModels.MeasurementUnit
import com.edurda77.domain.repository.MeasurementUnitRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.NEW_BASE_URL
import com.edurda77.domain.utils.ResultWork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
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

    override suspend fun insertUnit(
        accessToken: String,
        name: String,
        abbreviation: String,
    ): ResultWork<MeasurementUnit, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.post(NEW_BASE_URL + "measurement-units") {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                    setBody(
                        MeasurementUnitCreateRequest(
                            name = name,
                            abbreviation = abbreviation
                        )
                    )
                }
                result.call.body<MeasurementUnitDto>().toMeasurementUnit()
            }
        }
    }

    override suspend fun updateUnit(
        accessToken: String,
        measurementUnit: MeasurementUnit,
    ): ResultWork<MeasurementUnit, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.patch(NEW_BASE_URL + "measurement-units/"+measurementUnit.id) {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                    setBody(
                        measurementUnit.toMeasurementUnitDto()
                    )
                }
                result.call.body<MeasurementUnitDto>().toMeasurementUnit()
            }
        }
    }

    override suspend fun deleteUnit(
        accessToken: String,
        id: String
    ): ResultWork<Unit, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                httpClient.delete (NEW_BASE_URL + "measurement-units/"+id) {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                }
                Unit
            }
        }
    }
}