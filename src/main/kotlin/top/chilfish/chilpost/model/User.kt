package top.chilfish.chilpost.model

import kotlinx.serialization.Serializable

@Serializable
data class Owner(
    val id: Int,
    val name: String,
    val nickname: String,
    val avatar: String = "/placeholder.avatar.png",
)

@Serializable
data class User(
    val id: String= "",
    val name: String,
    val nickname: String,
    var password: String,
    val email: String,
    val avatar: String = "/placeholder.avatar.png",
    val bio: String = "hello",
    val level: String = "user",
)

/**
 * 用于更新用户信息
 */
@Serializable
data class UpdatedUser(
    val id: String,
    val name: String,
    val nickname: String,
//    val password: String,
    val email: String,
    val bio: String,
)
