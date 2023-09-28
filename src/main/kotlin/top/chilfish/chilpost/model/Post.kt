package top.chilfish.chilpost.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Post(
    val content: String,
    val ownerId: Int,
    val id: Int = 0,
    val isBody: Boolean = true,
    val parentId: Int = -1,

    val deleted: Boolean = false,
    val deletedAt: String = LocalDateTime.MIN.toString(),
    val createdAt: String = LocalDateTime.now().toString(),
)

@Serializable
data class PostStatus(
    val like_count: Int = 0,
    val comment_count: Int = 0,
    val repost_count: Int = 0,

    val likes: List<Int> = listOf(),
    val comments: List<Int> = listOf(),
    val reposts: List<Int> = listOf(),
)

data class PostDetails(
    val id: Int,
    val content: String,
    val ownerId: Int,
    val isBody: Boolean,
    val parentId: Int,
    val createdAt: LocalDateTime,
    val status: PostStatus,
    val owner: Owner
)