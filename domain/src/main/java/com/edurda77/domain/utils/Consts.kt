package com.edurda77.domain.utils

const val BASE_URL = "https://meteo.i-perm.ru:8001/api/"
const val BRADCASTING_URL = "https://meteo.i-perm.ru:8001/broadcasting/auth"
const val WEB_SOCKET_URL =
    "wss://meteo.i-perm.ru:6001/app/meteo?protocol=7&client=js&version=5.1.1&flash=false"
const val AUTH_POSTFIX = "auth/login"
const val AUTH_LOGGED_USER_POSTFIX = "auth/user"
const val DEVICES_POSTFIX = "devices"
const val NOTIFICATIONS_POSTFIX = "notifications"
const val PARAMS_POSTFIX = "params"
const val PARAMS_POSTFIX_MOBILE = "params-for-mobile"
const val DEVICES_GROUPS_POSTFIX = "devicesgroups"
const val PERMISSIONS_POSTFIX = "permissions"
const val USERS_POSTFIX = "users"
const val UNITS_POSTFIX = "units"
const val FAVORITE_POSTFIX = "favorites"
const val EMAIL = "email"
const val PASSWORD = "password"
const val PAGE_PARAMETER = "page"
const val FROM_DATE_PARAMETER = "fromDate"
const val TO_DATE_PARAMETER = "toDate"
const val LIMIT_PARAMETER = "limit"
const val SOCKET_ID_PARAMETER = "socket_id"
const val CHANNEL_NAME_PARAMETER = "channel_name"
const val CHANNEL_NAME_PREFIX = "private-device."
const val PARAMETER_GROUP = "groups"


const val APP_PREFERENCES = "auth_settings"
const val TOKEN_LABEL = "auth_token"
const val EXPIRED_LABEL = "expired_at"
const val USER_ID_LABEL = "user_id"
const val LAST_EMAIL = "last_email"
const val LAST_PASSWORD = "last_password"

const val NEGATIVE_ID = -1
const val TEMPERATURE_ID = 2

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
/*const val CAMERA_LIST = 11
const val CAMERA_CREATE = 12
const val CAMERA_EDIT = 13
const val CAMERA_DELETE = 14*/


const val STATUS_ON = "on"

const val IS_HIDDEN = 1

const val PING_INTERVAL = 20_000L
const val ESTABLISHED = "pusher:connection_established"
const val SUCCESSES = "subscription_succeeded"
const val DEVICE_EVENT = "DeviceEvent"
const val SUBSCRIBE_EVENT = "pusher:subscribe"
const val EVENT_CHANNEL_PREFIX = "private-device."
//const val MULTIPLE_SIZE = 10

const val DATABASE = "db_meteo"
const val FAVORITE_TABLE = "tb_favorite"
const val FAVORITE_DEVICE_ID = "device_id"
const val FAVORITE = "favorite"
const val FAVORITE_ID_GROUP = -10



//New
const val NEW_BASE_URL = "http://10.222.222.135:8081/api/"
const val ACCESS_TOKEN_LABEL = "access_token"
const val REFRESH_TOKEN_LABEL = "refresh_token"