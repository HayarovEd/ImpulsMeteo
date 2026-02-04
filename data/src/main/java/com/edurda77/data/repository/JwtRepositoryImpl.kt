package com.edurda77.data.repository

import android.util.Base64
import com.edurda77.domain.repository.JwtRepository
import org.json.JSONObject
import kotlin.text.split

class JwtRepositoryImpl: JwtRepository {

    override fun decodeJwt(token: String): Long? {
        return try {
            val parts = token.split(".")
            val payload = decodeBase64(parts[1])
            val jsonObject = JSONObject(payload)
            jsonObject.getLong("exp")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun decodeBase64(encoded: String): String {
        val decodedBytes = Base64.decode(encoded, Base64.URL_SAFE or Base64.NO_PADDING)
        return String(decodedBytes, Charsets.UTF_8)
    }
}