package com.edurda77.domain.usecase

import com.edurda77.domain.model.newModels.AuthUser
import com.edurda77.domain.model.newModels.Device
import com.edurda77.domain.model.newModels.Param
import com.edurda77.domain.model.newModels.User
import com.edurda77.domain.model.newModels.UserUi

object WsMessageFactory {

    fun updateDevice(
        devices: List<Device>,
        device: Device
    ): List<Device> {
        val deviceExists = devices.any { it.id == device.id }
        return if (deviceExists) {
            devices.map { dv ->
                if (dv.id == device.id) {
                    device
                } else dv
            }
        } else devices
    }

    fun deleteDevice(
        devices: List<Device>,
        id: String
    ): List<Device> {
        return devices.filter { it.id != id }
    }

    fun updateParam(
        devices: List<Device>,
        newParam: Param
    ): List<Device> {
        val paramExists =
            devices.any { it.params.any { deviceParam -> newParam.id == deviceParam.id } }
        return if (paramExists) {
            devices.map { device ->
                device.copy(
                    params = device.params.map { param ->
                        if (param.id == newParam.id) {
                            newParam
                        } else {
                            param
                        }
                    }
                )
            }
        } else devices
    }

    fun updateUser(
        users: List<UserUi>,
        newUser: UserUi
    ): List<UserUi> {
        val deviceExists = users.any { it.id == newUser.id }
        return if (deviceExists) {
            users.map { dv ->
                if (dv.id == newUser.id) {
                    newUser
                } else dv
            }
        } else users
    }

    fun updateAuthUser(
        authUser: AuthUser,
        newUser: User
    ): AuthUser {
        return if (authUser.id == newUser.id) {
            AuthUser(
                createdAt = newUser.createdAt,
                devices = newUser.devices,
                email = newUser.email,
                favorites = newUser.favorites,
                id = newUser.id,
                isEnabled = newUser.isEnabled,
                name = newUser.name,
                password = newUser.password,
                permissions = newUser.permissions,
                updateAt = newUser.updateAt,
            )
        } else authUser
    }

    fun deleteUser(
        users: List<User>,
        id: String
    ): List<User> {
        return users.filter { it.id != id }
    }

}