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

data class UserToken(val token: String, val user: User)

@Serializable
data class TokenData(val id: Int, val name: String)

data class CommentIds(val commentIds: List<Int>, val any: String?)

data class NewPostMeta(val type: String, val pcId: String?)

data class NewPost(val content: String, val meta: NewPostMeta)

data class IdJson(val id: String, val any: String?)