package com.edurda77.data.repository

import com.edurda77.data.handler.handleResponse
import com.edurda77.data.mapper.toDeviceUserRequest
import com.edurda77.data.mapper.toPermissionUserRequest
import com.edurda77.data.mapper.toUser
import com.edurda77.data.mapper.toUserUpdateRequest
import com.edurda77.data.remote.newDtos.user.UserCreateRequest
import com.edurda77.data.remote.newDtos.user.UserDto
import com.edurda77.domain.model.newModels.DeviceUser
import com.edurda77.domain.model.newModels.PermissionUser
import com.edurda77.domain.model.newModels.User
import com.edurda77.domain.model.newModels.UserUi
import com.edurda77.domain.repository.UsersRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.NEW_BASE_URL
import com.edurda77.domain.utils.ResultWork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsersRepositoryImpl(
    private val httpClient: HttpClient
) : UsersRepository {

    override suspend fun getUsers(
        accessToken: String,
    ): ResultWork<List<User>, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.get(NEW_BASE_URL + "users") {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                }
                result.call.body<List<UserDto>>().map { it.toUser() }
            }
        }
    }

    override suspend fun insertUser(
        accessToken: String,
        name: String,
        password: String,
        email: String,
        devices: List<DeviceUser>,
        permissions: List<PermissionUser>,
    ): ResultWork<User, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.post(NEW_BASE_URL + "users") {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                    setBody(
                        UserCreateRequest(
                            name = name,
                            email = email,
                            isEnabled = true,
                            password = password,
                            deviceUserRequests = devices.map { it.toDeviceUserRequest() },
                            permissionUserRequests = permissions.map { it.toPermissionUserRequest() }
                        )
                    )
                }
                result.call.body<UserDto>().toUser()
            }
        }
    }

    override suspend fun updateUser(
        accessToken: String,
        userUi: UserUi,
    ): ResultWork<User, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.patch(NEW_BASE_URL + "users/"+userUi.id) {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                    setBody(
                        userUi.toUserUpdateRequest()
                    )
                }
                result.call.body<UserDto>().toUser()
            }
        }
    }
}