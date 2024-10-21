package com.edurda77.domain.utils

const val BASE_URL = "https://meteo.i-perm.ru:8001/api/"
const val WEB_SOCKET_URL =
    "wss://meteo.i-perm.ru:6001/app/meteo?protocol=7&client=js&version=5.1.1&flash=false"
const val AUTH_POSTFIX = "auth/login"
const val AUTH_LOGGED_USER_POSTFIX = "auth/user"
const val DEVICES_POSTFIX = "devices"
const val DEVICES_GROUPS_POSTFIX = "devicesgroups"
const val PERMISSIONS_POSTFIX = "permissions"
const val USERS_POSTFIX = "users"
const val UNITS_POSTFIX = "units"
const val EMAIL = "email"
const val PASSWORD = "password"
const val PAGE_PARAMETER = "page"

const val PARAMETER_GROUP = "groups"


const val APP_PREFERENCES = "auth_settings"
const val TOKEN_LABEL = "auth_token"
const val EXPIRED_LABEL = "expired_at"
const val USER_ID_LABEL = "user_id"
const val LAST_EMAIL = "last_email"
const val LAST_PASSWORD = "last_password"

const val NEGATIVE_USER_ID = -1

//permissions
const val USERS_LIST = 1
const val USERS_CREATE = 2
const val USERS_EDIT = 3
const val USERS_DELETE = 4
const val DEVICES_LIST = 5
const val DEVICES_CREATE = 6
const val DEVICES_EDIT = 7
const val DEVICES_DELETE = 8
const val DIRECTORY_LIST = 9
const val DIRECTORY_EDIT = 10
const val CAMERA_LIST = 11
const val CAMERA_CREATE = 12
const val CAMERA_EDIT = 13
const val CAMERA_DELETE = 14


const val STATUS_ON = "on"


const val PING_INTERVAL = 20_000L
const val SUBSCRIBE = "pusher:subscribe"
const val EVENT_CHANNEL_PREFIX = "private-device."
//const val MULTIPLE_SIZE = 10
