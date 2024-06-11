package com.fithlanz.fithlanz.Volley

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

interface ServidorCentral{
    val direccionServidorCentral: String
        get() = "http://3.212.167.151:3000"
}

fun exchangeAuthCodeForAccessToken(serverAuthCode: String, context: Context, callback: TokenCallback) {
    val url = "https://cliente-kxx4dhwdza-no.a.run.app/exchangeAuthCode"

    val requestBody = JSONObject().apply {
        put("authCode", serverAuthCode)
    }

    val request = JsonObjectRequest(
        Request.Method.POST, url, requestBody,
        { response ->
            val refreshToken = response.getString("refreshToken")
            Log.d(ContentValues.TAG, "Refresh Token: $refreshToken")
            callback.onTokenReceived(refreshToken)
        },
        { error ->
            Log.e(ContentValues.TAG, "Error exchanging auth code: ${error.message}")
            callback.onError(error.message ?: "Unknown error")
        }
    )

    Volley.newRequestQueue(context).add(request)
}
interface TokenCallback {
    fun onTokenReceived(refreshToken: String)
    fun onError(error: String)
}
