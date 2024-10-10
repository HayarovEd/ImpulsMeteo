package com.edurda77.domain.utils


fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$")
    return email.matches(emailRegex)
}
/*

@OptIn(ExperimentalEncodingApi::class)
fun decodeToken(jwt: String): String {
    val parts = jwt.split(".")
    return try {
        val header = String(Base64.decode(parts[0]))
        val payload = String(Base64.decode(parts[1]))
        //"$header"
        //"$payload"

        println("payload $payload")
        payload
    } catch (e: Exception) {
        "Error parsing JWT: $e"
    }
}*/
