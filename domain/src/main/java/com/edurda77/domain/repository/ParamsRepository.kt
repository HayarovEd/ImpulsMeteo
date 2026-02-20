package com.edurda77.domain.repository

import com.edurda77.domain.model.newModels.History
import com.edurda77.domain.model.newModels.Param
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork

interface ParamsRepository {
    suspend fun getHistoryDeviceById(
        accessToken: String,
        deviceId: String,
        fromDate: String,
        toDate: String,
        limit: Int
    ): ResultWork<Map<String, List<History>>, DataError>

    suspend fun updateParam(accessToken: String, param: Param): ResultWork<Param, DataError>
    suspend fun clearHistory(accessToken: String, ids: List<String>): ResultWork<Unit, DataError>
}