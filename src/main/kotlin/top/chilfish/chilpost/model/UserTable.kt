package top.chilfish.chilpost.model

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.json.json
import org.jetbrains.exposed.sql.select
import top.chilfish.chilpost.dao.isFollowing
import top.chilfish.chilpost.dao.userDetail
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.newError
import java.time.LocalDateTime

object UserTable : IntIdTable("users") {
    val uuid = uuid("uuid").index()
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


fun toUserDetail(it: ResultRow, uid: Int = -1) = mapOf(
    "id" to it[UserTable.uuid],
    "name" to it[UserTable.name],
    "nickname" to it[UserTable.nickname],
    "email" to it[UserTable.email],
    "password" to it[UserTable.password],
    "avatar" to it[UserTable.avatar],
    "bio" to it[UserTable.bio],

//    "level" to it[UserTable.level],
//    "deleted" to it[UserTable.deleted],
//    "createdAt" to it[UserTable.createdAt],
    "status" to mapOf(
        "is_following" to isFollowing(uid, it[UserTable.id].value),
        "post_count" to it[UserStatusT.postCount],
        "follower_count" to it[UserStatusT.followerCount],
        "following_count" to it[UserStatusT.followingCount],
//        "followers" to it[UserStatusT.followers],
//        "followings" to it[followings],
    ),
)

fun getUserDetail(name: String, uid: Int = -1): Map<String, Any> {
    val res = (
            userDetail()
                .select { UserTable.name eq name }
                .withDistinct()
                .map { toUserDetail(it, uid) }
                .firstOrNull() ?: throw newError(ErrorCode.NOT_FOUND_USER)
            ).toMutableMap()
        .minus("password")

    return res
}