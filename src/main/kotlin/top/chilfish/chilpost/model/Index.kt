package top.chilfish.chilpost.model

import kotlinx.serialization.Serializable
import top.chilfish.chilpost.error.ErrorCode

data class AuthData(val email: String, val password: String)

data class ApiReturn<T>(
    val code: ErrorCode = ErrorCode.OK,
    val statusCode: Int = 200,
    val message: String = "ok",
    val data: T? = null
)

data class UserToken(val token: String, val user: UserDetails)

@Serializable
data class TokenData(val id: Int, val name: String)