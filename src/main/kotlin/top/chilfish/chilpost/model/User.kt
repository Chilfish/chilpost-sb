package top.chilfish.chilpost.model

import kotlinx.serialization.Serializable
import java.util.*

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

//    val uuid: String = UUID.randomUUID().toString(),

//    val created_at: String = LocalDateTime.now().toString(),
//    val deleted: Boolean = false,

    val status: UserStatus? = UserStatus(),
)

@Serializable
data class UserStatus(
    val post_count: Int = 0,
    val follower_count: Int = 0,
    val following_count: Int = 0,

    val followers: List<Int> = listOf(),
    val followings: List<Int> = listOf(),
)
