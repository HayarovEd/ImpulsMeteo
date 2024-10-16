package com.edurda77.data.repository

import com.edurda77.data.handler.handleResponse
import com.edurda77.data.mapper.convertToAuth
import com.edurda77.data.mapper.convertToDevices
import com.edurda77.data.mapper.convertToGroups
import com.edurda77.data.mapper.convertToLoggedUser
import com.edurda77.data.mapper.convertToPermissions
import com.edurda77.data.mapper.convertToUsers
import com.edurda77.data.remote.add_device.AddDeviceDto
import com.edurda77.data.remote.auth.AuthDto
import com.edurda77.data.remote.auth_user.AuthUserDto
import com.edurda77.data.remote.devices.DevicesDto
import com.edurda77.data.remote.group.DevicesGropusDto
import com.edurda77.data.remote.permission.PermissionsDto
import com.edurda77.data.remote.user.UsersDto
import com.edurda77.domain.model.Auth
import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.model.LoggedUser
import com.edurda77.domain.model.Permissions
import com.edurda77.domain.model.User
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.AUTH_LOGGED_USER_POSTFIX
import com.edurda77.domain.utils.AUTH_POSTFIX
import com.edurda77.domain.utils.BASE_URL
import com.edurda77.domain.utils.DEVICES_GROUPS_POSTFIX
import com.edurda77.domain.utils.DEVICES_POSTFIX
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.EMAIL
import com.edurda77.domain.utils.PAGE_PARAMETR
import com.edurda77.domain.utils.PARAMETER_GROUP
import com.edurda77.domain.utils.PASSWORD
import com.edurda77.domain.utils.PERMISSIONS_POSTFIX
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.USERS_POSTFIX
import com.edurda77.domain.utils.convertToMapGroupedDevices
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient
) : RemoteRepository {

    override suspend fun authorization(
        email: String,
        password: String,
    ): ResultWork<Auth, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.submitForm(
                    url = BASE_URL + AUTH_POSTFIX,
                    formParameters = parameters {
                        append(EMAIL, email)
                        append(PASSWORD, password)
                    }
                ).call
                    .body<AuthDto>()
                result.convertToAuth()
            }
        }
    }

    override suspend fun authorizedUser(
        token: String,
    ): ResultWork<LoggedUser, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.get(BASE_URL + AUTH_LOGGED_USER_POSTFIX) {
                    url {
                        bearerAuth(token)
                    }
                }.call
                    .body<AuthUserDto>()
                result.convertToLoggedUser()
            }
        }
    }

    override suspend fun getGroupedDevices(
        token: String,
        parameterGroup: Int
    ): ResultWork<Map<GroupDevices, List<Device>>, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val responseDevices = httpClient.get(BASE_URL + DEVICES_POSTFIX) {
                    url {
                        bearerAuth(token)
                        parameter(PARAMETER_GROUP, "[$parameterGroup]")
                    }
                }.call
                    .body<DevicesDto>()
                val devices = responseDevices.convertToDevices()
                val responseGroups = httpClient.get(BASE_URL + DEVICES_GROUPS_POSTFIX) {
                    url {
                        bearerAuth(token)
                    }
                }.call
                    .body<DevicesGropusDto>()
                val groups = responseGroups.convertToGroups()
                convertToMapGroupedDevices(
                    groups = groups,
                    devices = devices
                )
            }
        }
    }

    override suspend fun addDevice(
        token: String,
        groups: List<Int>,
        key: String,
        name: String,
        update: String
    ): ResultWork<Unit, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                httpClient.post(BASE_URL + DEVICES_POSTFIX) {
                    contentType(ContentType.Application.Json)
                    url {
                        bearerAuth(token)
                        setBody(
                            AddDeviceDto(
                                groups = groups,
                                key = key,
                                name = name,
                                update = update
                            )
                        )
                    }
                }.bodyAsText()
                Unit
            }
        }
    }

    override suspend fun getPermissions(
        token: String,
    ): ResultWork<Permissions, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.get(BASE_URL + PERMISSIONS_POSTFIX) {
                    url {
                        bearerAuth(token)
                    }
                }.call
                    .body<PermissionsDto>()
                result.convertToPermissions()
            }
        }
    }

    override suspend fun getUsers(
        token: String,
    ): ResultWork<List<User>, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val users = mutableListOf<User>()
                val resultFirst = httpClient.get(BASE_URL + USERS_POSTFIX) {
                    url {
                        bearerAuth(token)
                        parameter(PAGE_PARAMETR, 1)
                    }
                }.call
                    .body<UsersDto>()
                users.addAll(resultFirst.convertToUsers())
                var nextUrl = resultFirst.nextPageUrl
                while (nextUrl != null) {
                    val nextResult = httpClient.get(nextUrl) {
                        url {
                            bearerAuth(token)
                        }
                    }.call
                        .body<UsersDto>()
                    users.addAll(nextResult.convertToUsers())
                    nextUrl = nextResult.nextPageUrl
                }
                users
            }
        }
    }
}