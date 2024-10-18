package com.edurda77.login_screen

import com.edurda77.resources.uikit.UiText


sealed class LoginEvent {
    class SetEmail(val email:String): LoginEvent()
    class SetPassword(val password:String): LoginEvent()
    data object OnLogin: LoginEvent()
}


sealed class UiLoginEvents {
    data class SnackbarEvent(val message : UiText?) : UiLoginEvents()
    data object NavigateEvent : UiLoginEvents()
}