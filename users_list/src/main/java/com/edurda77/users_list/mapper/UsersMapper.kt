package com.edurda77.users_list.mapper

import com.edurda77.domain.model.UserOld
import com.edurda77.users_list.model.UserUi

fun UserOld.convertToUi(): UserUi {
    return UserUi(
        email = this.email,
        id = this.id,
        name = this.name,
        devices = this.devices,
        permissions = this.permissions
    )
}