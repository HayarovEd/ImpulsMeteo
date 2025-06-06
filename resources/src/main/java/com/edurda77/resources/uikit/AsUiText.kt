package com.edurda77.resources.uikit

import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import com.edurda77.resources.R


fun DataError.asUiText(): UiText {
    return when (this) {

        DataError.Network.NO_INTERNET -> UiText.StringResource(
            R.string.no_internet
        )

        DataError.Network.SERVER_ERROR -> UiText.StringResource(
            R.string.server_error
        )

        DataError.Network.UNKNOWN -> UiText.DynamicString("")

        DataError.EmailError.EMAIL_BLANK -> UiText.StringResource(
            R.string.email_empty
        )
        DataError.EmailError.EMAIL_NOT_VALID -> UiText.StringResource(
            R.string.email_unvalid
        )
        DataError.Network.UNAUTHORIZED -> UiText.StringResource(
            R.string.error_autorization
        )
        DataError.PasswordError.PASSWORD_BLANK -> UiText.StringResource(
            R.string.password_blank
        )

        DataError.IniqueIdError.INIQUEID_BLANK -> {
            UiText.StringResource(
                R.string.unique_id_is_blank
            )
        }
        DataError.NameError.NAME_BLANK -> {
            UiText.StringResource(
                R.string.name_is_blank
            )
        }
        DataError.Network.BAD_REQUEST -> {
            UiText.StringResource(
                R.string.not_unique_id
            )
        }
        DataError.DataStore.ERROR_READ_DATA -> {
            UiText.StringResource(
                R.string.read_error_data
            )
        }

        DataError.DataStore.ERROR_WRITE_DATA -> {
            UiText.StringResource(
                R.string.write_error_data
            )
        }
        DataError.Network.NOT_FOUND -> {
            UiText.StringResource(
                R.string.not_found
            )
        }
        DataError.SerializationError.FORMAT_ERROR -> {
            UiText.StringResource(
                R.string.error_take_data
            )
        }

        DataError.NameError.ID_ERROR -> {
            UiText.StringResource(
                R.string.no_access
            )
        }

        DataError.WebSocketError.NOT_CONNECT -> {
            UiText.StringResource(
                R.string.no_connect
            )
        }
        DataError.WebSocketError.PUSHER_ERROR -> {
            UiText.StringResource(
                R.string.bad_request
            )
        }

        DataError.LocalDateBase.ERROR_READ_DATA -> {
            UiText.StringResource(
                R.string.read_error_data
            )
        }

        DataError.LocalDateBase.ERROR_WRITE_DATA -> {
            UiText.StringResource(
                R.string.read_error_data
            )
        }

        DataError.Network.REQUEST_TIMEOUT -> {
            UiText.StringResource(
                R.string.request_timeout
            )
        }
    }
}

fun ResultWork.Error<*, DataError>.asErrorUiText(): UiText {
    return error.asUiText()
}