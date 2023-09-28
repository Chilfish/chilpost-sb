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
    val id: Int,
    val name: String,
    val nickname: String,
    val password: String,
    val email: String,
    val avatar: String = "/placeholder.avatar.png",
    val bio: String = "hello",
    val level: String = "user",

    val createdAt: String,
    val updatedAt: String,
    val deleted: Boolean,
    val deletedAt: String,
)

@Serializable
data class UserStatus(
    val post_count: Int = 0,
    val follower_count: Int = 0,
    val following_count: Int = 0,

    val followers: List<Int> = listOf(),
    val following: List<Int> = listOf(),
)
