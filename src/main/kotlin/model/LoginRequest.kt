// LoginRequest.kt
package model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val user: String,
    val passwd: String
)
