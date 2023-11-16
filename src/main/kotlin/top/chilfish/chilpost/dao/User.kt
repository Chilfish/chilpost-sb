package top.chilfish.chilpost.dao

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.newError
import top.chilfish.chilpost.model.User
import top.chilfish.chilpost.model.UserStatusT
import top.chilfish.chilpost.model.UserTable

fun toUserDetail(it: ResultRow) = mapOf(
    "id" to it[UserTable.id].value,
    "name" to it[UserTable.name],
    "nickname" to it[UserTable.nickname],
    "email" to it[UserTable.email],
    "password" to it[UserTable.password],
    "avatar" to it[UserTable.avatar],
    "bio" to it[UserTable.bio],
    "level" to it[UserTable.level],
//    "deleted" to it[UserTable.deleted],
//    "createdAt" to it[UserTable.createdAt],
    "status" to mapOf(
        "post_count" to it[UserStatusT.postCount],
        "follower_count" to it[UserStatusT.followerCount],
        "following_count" to it[UserStatusT.followingCount],
        "followers" to it[UserStatusT.followers],
        "followings" to it[UserStatusT.followings],
    ),
)

fun getUserDetail(name: String): MutableMap<String, Any> {
    val res = ((UserTable innerJoin UserStatusT)
        .select { UserTable.name eq name }
        .withDistinct()
        .map(::toUserDetail)
        .firstOrNull() ?: throw newError(ErrorCode.NOT_FOUND_USER))
        .toMutableMap()

    res["password"] = "******"

    return res
}

fun getUserByEmail(email: String) = (UserTable innerJoin UserStatusT)
    .select { UserTable.email eq email }
    .map(::toUserDetail)
    .firstOrNull() ?: throw newError(ErrorCode.NOT_FOUND_USER)

fun addUser(name: String, nickname: String, email: String, password: String): Int {
    val id = UserTable
        .insertAndGetId {
            it[UserTable.name] = name
            it[UserTable.nickname] = nickname
            it[UserTable.password] = password
            it[UserTable.email] = email
        }.value

    UserStatusT.insert { it[userId] = id }

    return id
}