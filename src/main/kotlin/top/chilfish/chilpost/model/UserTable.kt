package top.chilfish.chilpost.model

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.json.json
import java.time.LocalDateTime

object UserTable : IntIdTable("users") {
    val uuid  = uuid("uuid").index()
    val name = varchar("name", 255).index()
    val nickname = varchar("nickname", 255)
    val password = varchar("password", 255)
    val email = varchar("email", 255)
    val bio = varchar("bio", 255).default("hello")
    val avatar = varchar("avatar", 255).default("/placeholder.avatar.png")
    val level = varchar("level", 255).default("user")

    val deleted = bool("deleted").default(false)
    val deletedAt = datetime("deleted_at").nullable().default(null)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())

    init {
        uniqueIndex(name, email)
    }
}

object UserStatusT : IntIdTable("user_status") {
    val userId = reference("user_id", UserTable.id).index()
    val postCount = integer("post_count").default(0)
    val followerCount = integer("follower_count").default(0)
    val followingCount = integer("following_count").default(0)
    val followers = json<Array<String>>("followers", Json.Default).default(arrayOf())
    val followings = json<Array<String>>("followings", Json.Default).default(arrayOf())
}
