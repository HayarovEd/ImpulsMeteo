package com.edurda77.data.repository

import com.edurda77.data.handler.handleResponse
import com.edurda77.data.mapper.convertToAuth
import com.edurda77.data.mapper.convertToDevices
import com.edurda77.data.mapper.convertToGroups
import com.edurda77.data.mapper.convertToLoggedUser
import com.edurda77.data.mapper.convertToPermissions
import com.edurda77.data.mapper.convertToSingleDevice
import com.edurda77.data.mapper.convertToUnits
import com.edurda77.data.mapper.convertToUsers
import com.edurda77.data.remote.add_device.AddDeviceDto
import com.edurda77.data.remote.add_devices_group.AddDevicesGroupDto
import com.edurda77.data.remote.add_unit.AddUnitDto
import com.edurda77.data.remote.add_user.AddUserDto
import com.edurda77.data.remote.auth.AuthDto
import com.edurda77.data.remote.auth_user.AuthUserDto
import com.edurda77.data.remote.device.BodyDeviceDto
import com.edurda77.data.remote.devices.DevicesDto
import com.edurda77.data.remote.group.DevicesGropusDto
import com.edurda77.data.remote.permission.PermissionsDto
import com.edurda77.data.remote.units.UnitsDto
import com.edurda77.data.remote.update_devices_group.UpdateDevicesGroupDto
import com.edurda77.data.remote.update_unit.UpdateUnitDto
import com.edurda77.data.remote.update_user.UpdateUserDto
import com.edurda77.data.remote.user.UsersDto
import com.edurda77.domain.model.Auth
import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.model.LoggedUser
import com.edurda77.domain.model.Permissions
import com.edurda77.domain.model.SingleDevice
import com.edurda77.domain.model.UnitMeteo
import com.edurda77.domain.model.User
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.AUTH_LOGGED_USER_POSTFIX
import com.edurda77.domain.utils.AUTH_POSTFIX
import com.edurda77.domain.utils.BASE_URL
import com.edurda77.domain.utils.DEVICES_GROUPS_POSTFIX
import com.edurda77.domain.utils.DEVICES_POSTFIX
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.EMAIL
import com.edurda77.domain.utils.PAGE_PARAMETER
import com.edurda77.domain.utils.PARAMETER_GROUP
import com.edurda77.domain.utils.PASSWORD
import com.edurda77.domain.utils.PERMISSIONS_POSTFIX
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.UNITS_POSTFIX
import com.edurda77.domain.utils.USERS_POSTFIX
import com.edurda77.domain.utils.convertToMapGroupedDevices
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
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
                convertToMapGroupedDevices(
                    devices = devices
                )
            }
        }
    }

    override suspend fun getDeviceById(
        token: String,
        id: Int,
    ): ResultWork<SingleDevice, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val responseDevices = httpClient.get("$BASE_URL$DEVICES_POSTFIX/$id") {
                    url {
                        bearerAuth(token)
                    }
                }.call

                println("TEST DEVECE DETAIL SCREEN, ${responseDevices.response.body<String>()}")
                responseDevices
                    .body<BodyDeviceDto>().convertToSingleDevice()
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
                        parameter(PAGE_PARAMETER, 1)
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

    override suspend fun addUser(
        token: String,
        devices: List<String>,
        permissions: List<String>,
        email: String,
        name: String,
        password: String,
    ): ResultWork<Unit, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                httpClient.post(BASE_URL + USERS_POSTFIX) {
                    contentType(ContentType.Application.Json)
                    url {
                        bearerAuth(token)
                        setBody(
                            AddUserDto(
                                devices = devices,
                                permissions = permissions,
                                name = name,
                                email = email,
                                password = password
                            )
                        )
                    }
                }.bodyAsText()
                Unit
            }
        }
    }

    override suspend fun deleteUser(
        token: String,
        id: Int
    ): ResultWork<Unit, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                httpClient.delete("$BASE_URL$USERS_POSTFIX/$id") {
                    contentType(ContentType.Application.Json)
                    url {
                        bearerAuth(token)
                    }
                }.bodyAsText()
                Unit
            }
        }
    }

    override suspend fun updateUser(
        token: String,
        id: Int,
        devices: List<String>,
        permissions: List<String>,
        name: String,
        email: String,
        password: String
    ): ResultWork<Unit, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                httpClient.put("$BASE_URL$USERS_POSTFIX/$id") {
                    contentType(ContentType.Application.Json)
                    url {
                        bearerAuth(token)
                        setBody(
                            UpdateUserDto(
                                id = id,
                                devices = devices,
                                permissions = permissions,
                                name = name,
                                email = email,
                                password = password
                            )
                        )
                    }
                }.bodyAsText()
                Unit
            }
        }
    }

    override suspend fun getDevicesGroups(
        token: String,
    ): ResultWork<List<GroupDevices>, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val responseGroups = httpClient.get(BASE_URL + DEVICES_GROUPS_POSTFIX) {
                    url {
                        bearerAuth(token)
                    }
                }.call
                    .body<DevicesGropusDto>()
                responseGroups.convertToGroups()
            }
        }
    }

    override suspend fun addDevicesGroup(
        token: String,
        name: String,
    ): ResultWork<Unit, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                httpClient.post(BASE_URL + DEVICES_GROUPS_POSTFIX) {
                    contentType(ContentType.Application.Json)
                    url {
                        bearerAuth(token)
                        setBody(
                            AddDevicesGroupDto(
                                name = name,
                            )
                        )
                    }
                }.bodyAsText()
                Unit
            }
        }
    }

    override suspend fun deleteDevicesGroup(
        token: String,
        id: Int
    ): ResultWork<Unit, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                httpClient.delete("$BASE_URL$DEVICES_GROUPS_POSTFIX/$id") {
                    contentType(ContentType.Application.Json)
                    url {
                        bearerAuth(token)
                    }
                }.bodyAsText()
                Unit
            }
        }
    }

    override suspend fun updateDevicesGroup(
        token: String,
        id: Int,
        name: String,
    ): ResultWork<Unit, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                httpClient.put("$BASE_URL$DEVICES_GROUPS_POSTFIX/$id") {
                    contentType(ContentType.Application.Json)
                    url {
                        bearerAuth(token)
                        setBody(
                            UpdateDevicesGroupDto(
                                id = id,
                                name = name,
                            )
                        )
                    }
                }.bodyAsText()
                Unit
            }
        }
    }

    override suspend fun getUnits(
        token: String,
    ): ResultWork<List<UnitMeteo>, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val responseGroups = httpClient.get(BASE_URL + UNITS_POSTFIX) {
                    url {
                        bearerAuth(token)
                    }
                }.call
                    .body<UnitsDto>()
                responseGroups.convertToUnits()
            }
        }
    }


    override suspend fun addUnit(
        token: String,
        name: String,
        short: String,
    ): ResultWork<Unit, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                httpClient.post(BASE_URL + UNITS_POSTFIX) {
                    contentType(ContentType.Application.Json)
                    url {
                        bearerAuth(token)
                        setBody(
                            AddUnitDto(
                                name = name,
                                short = short
                            )
                        )
                    }
                }.bodyAsText()
                Unit
            }
        }
    }

    override suspend fun deleteUnit(
        token: String,
        id: Int
    ): ResultWork<Unit, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                httpClient.delete("$BASE_URL$UNITS_POSTFIX/$id") {
                    contentType(ContentType.Application.Json)
                    url {
                        bearerAuth(token)
                    }
                }.bodyAsText()
                Unit
            }
        }
    }

    override suspend fun updateUnit(
        token: String,
        id: Int,
        name: String,
        short: String,
    ): ResultWork<Unit, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                httpClient.put("$BASE_URL$UNITS_POSTFIX/$id") {
                    contentType(ContentType.Application.Json)
                    url {
                        bearerAuth(token)
                        setBody(
                            UpdateUnitDto(
                                id = id,
                                name = name,
                                short = short
                            )
                        )
                    }
                }.bodyAsText()
                Unit
            }
        }
    }

    /* override suspend fun getHistoryDeviceById(
         token: String,
         id: Int,
         fromDate: String,
         toDate: String,
         limit: Int,
     ): ResultWork<SingleDevice, DataError> {
         return withContext(Dispatchers.IO) {
             handleResponse {
                 val responseDevices = httpClient.get("$BASE_URL$DEVICES_POSTFIX/$id/$PARAMS_POSTFIX") {
                     url {
                         bearerAuth(token)
                         parameter(FROM_DATE_PARAMETER, fromDate)
                         parameter(TO_DATE_PARAMETER, toDate)
                     }
                 }.call
                     .body<BodyDeviceDto>()
                 responseDevices.convertToSingleDevice()
             }
         }
     }*/
}