package network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import model.LoginRequest
import model.User
import network.NetworkUtils.httpClient
import utils.sha512

fun logInAPI(
    usuario: String,
    password: String,
    onSuccessResponse: (User) -> Unit,
    onFailure: (String) -> Unit
) {
    val url = "http://127.0.0.1:5000/login"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(usuario, sha512(password)))
            }
            if (response.status == HttpStatusCode.OK) {
                val user = response.body<User>()
                withContext(Dispatchers.Main) {
                    onSuccessResponse(user)
                }
            } else {
                val errorBody = response.bodyAsText()
                withContext(Dispatchers.Main) {
                    onFailure("Error: ${response.status}, Body: $errorBody")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onFailure("Exception: ${e.localizedMessage}")
            }
        }
    }
}
