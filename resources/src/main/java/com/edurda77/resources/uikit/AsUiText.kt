package com.edurda77.resources.uikit

import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import com.edurda77.resources.R
import com.edurda77.resources.uikit.UiText.*


fun DataError.asUiText(): UiText {
    return when (this) {

        DataError.Network.NO_INTERNET -> StringResource(
            R.string.no_internet
        )

        DataError.Network.SERVER_ERROR -> StringResource(
            R.string.server_error
        )

        DataError.Network.UNKNOWN -> DynamicString("")

        DataError.EmailError.EMAIL_BLANK -> StringResource(
            R.string.email_empty
        )
        DataError.EmailError.EMAIL_NOT_VALID -> StringResource(
            R.string.email_unvalid
        )
        DataError.Network.UNAUTHORIZED -> StringResource(
            R.string.error_autorization
        )
        DataError.PasswordError.PASSWORD_BLANK -> StringResource(
            R.string.password_blank
        )

        DataError.IniqueIdError.INIQUEID_BLANK -> {
            StringResource(
                R.string.unique_id_is_blank
            )
        }
        DataError.NameError.NAME_BLANK -> {
            StringResource(
                R.string.name_is_blank
            )
        }
        DataError.Network.BAD_REQUEST -> {
            StringResource(
                R.string.not_unique_id
            )
        }
        DataError.DataStore.ERROR_READ_DATA -> {
            StringResource(
                R.string.read_error_data
            )
        }

        DataError.DataStore.ERROR_WRITE_DATA -> {
            StringResource(
                R.string.write_error_data
            )
        }
        DataError.Network.NOT_FOUND -> {
            StringResource(
                R.string.not_found
            )
        }
        DataError.SerializationError.FORMAT_ERROR -> {
            StringResource(
                R.string.error_take_data
            )
        }

        DataError.NameError.ID_ERROR -> {
            StringResource(
                R.string.no_access
            )
        }

        DataError.WebSocketError.NOT_CONNECT -> {
            StringResource(
                R.string.no_connect
            )
        }
        DataError.WebSocketError.PUSHER_ERROR -> {
            StringResource(
                R.string.bad_request
            )
        }

        DataError.LocalDateBase.ERROR_READ_DATA -> {
            StringResource(
                R.string.read_error_data
            )
        }

        DataError.LocalDateBase.ERROR_WRITE_DATA -> {
            StringResource(
                R.string.read_error_data
            )
        }

        DataError.Network.REQUEST_TIMEOUT -> {
            StringResource(
                R.string.request_timeout
            )
        }

        DataError.TokenError.TOKEN_EXPIRED -> {
            StringResource(
                R.string.error_autorization
            )
        }
    }
}

fun ResultWork.Error<*, DataError>.asErrorUiText(): UiText {
    return error.asUiText()
}