package com.edurda77.domain.utils

sealed interface DataError : RootError {
    enum class Network: DataError {
        NO_INTERNET,
        UNAUTHORIZED,
        SERVER_ERROR,
        BAD_REQUEST,
        NOT_FOUND,
        UNKNOWN
    }

    enum class WebSocketError : DataError {
        NOT_CONNECT,
        PUSHER_ERROR,
    }

    enum class SerializationError: DataError {
       FORMAT_ERROR,
    }

    enum class EmailError: DataError {
        EMAIL_BLANK,
        EMAIL_NOT_VALID,
    }

    enum class PasswordError: DataError {
        PASSWORD_BLANK,
    }

    enum class NameError: DataError {
        NAME_BLANK,
        ID_ERROR,
    }

    enum class IniqueIdError: DataError {
        INIQUEID_BLANK,
    }

    enum class  DataStore: DataError {
        ERROR_READ_DATA,
        ERROR_WRITE_DATA
    }
}