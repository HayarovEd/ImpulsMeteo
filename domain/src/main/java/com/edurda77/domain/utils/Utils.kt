package com.edurda77.domain.utils


fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$")
    return email.matches(emailRegex)
}

/*
@OptIn(ExperimentalEncodingApi::class)
private fun decodeToken(jwt: String): String {
    val parts = jwt.split(".")
    return try {
        val charset = charset("UTF-8")
        val header = String(Base64.decode(parts[0].toByteArray(charset)), charset)
        val payload = String(Base64.decode(parts[1].toByteArray(charset)), charset)
        "$header"
        "$payload"
    } catch (e: Exception) {
        "Error parsing JWT: $e"
    }
}*/
