package top.chilfish.chilpost.model

import kotlinx.serialization.Serializable

data class AuthData(val email: String, val password: String)

data class ApiReturn<T>(
    val code: String = "ok",
    val statusCode: Int = 200,
    val message: String = "ok",
    val data: T
)

data class UserToken(val token: String, val user: UserDetails)

@Serializable
data class TokenData(val id: Int, val username: String)