package top.chilfish.chilpost.model

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.json.json
import java.time.LocalDateTime

object UserEntity : IntIdTable("users") {
    val name = varchar("name", 255)
    val nickname = varchar("nickname", 255)
    val password = varchar("password", 255)
    val email = varchar("email", 255)
    val bio = varchar("bio", 255).default("hello")
    val avatar = varchar("avatar", 255).default("/placeholder.avatar.png")
    val level = varchar("level", 255).default("user")

    val deleted = bool("deleted").default(false)
    val deletedAt = datetime("deleted_at").default(LocalDateTime.now())
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())

//    val status = text("status")
}

object UserStatusT : IntIdTable("user_status") {
    val userId = reference("user_id", UserEntity.id)
    val postCount = integer("post_count").default(0)
    val followerCount = integer("follower_count").default(0)
    val followingCount = integer("following_count").default(0)
    val followers = json<IntArray>("followers", Json.Default).default(intArrayOf())
    val followings = json<IntArray>("followings", Json.Default).default(intArrayOf())
}

fun List<ResultRow>.toUser() = this.map {
    UserDetails(
        id = it[UserEntity.id].value,
        name = it[UserEntity.name],
        nickname = it[UserEntity.nickname],
        password = it[UserEntity.password],
        email = it[UserEntity.email],
        avatar = it[UserEntity.avatar],
        bio = it[UserEntity.bio],
        level = it[UserEntity.level],
        deleted = it[UserEntity.deleted],
        createdAt = it[UserEntity.createdAt],
//        status = Json.decodeFromString(it[UserEntity.status])
    )
}