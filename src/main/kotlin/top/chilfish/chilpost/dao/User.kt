package top.chilfish.chilpost.dao

import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.newError
import top.chilfish.chilpost.model.UserTable
import top.chilfish.chilpost.model.UserStatusT
import top.chilfish.chilpost.model.toUser

fun getUserDetail(name: String): List<Map<String, Any>> {
    val res = (UserTable innerJoin UserStatusT)
        .select { UserTable.name eq name }
        .withDistinct()
        .map {
            mapOf(
                "id" to it[UserTable.id].value,
                "name" to it[UserTable.name],
                "nickname" to it[UserTable.nickname],
                "email" to it[UserTable.email],
                "avatar" to it[UserTable.avatar],
                "bio" to it[UserTable.bio],
                "deleted" to it[UserTable.deleted],
                "createdAt" to it[UserTable.createdAt],
                "status" to mapOf(
                    "post_count" to it[UserStatusT.postCount],
                    "follower_count" to it[UserStatusT.followerCount],
                    "following_count" to it[UserStatusT.followingCount],
                    "followers" to it[UserStatusT.followers],
                    "followings" to it[UserStatusT.followings],
                ),
            )
        }

    if (res.first()["deleted"] == true) {
        throw newError(ErrorCode.NOT_FOUND_USER)
    }

    return res
}

fun getUserByEmail(email: String) = UserTable
    .select { UserTable.email eq email }
    .firstOrNull()
    ?.toUser()

fun addUser(name: String, nickname: String, email: String, password: String) = UserTable
    .insertAndGetId {
        it[UserTable.name] = name
        it[UserTable.nickname] = nickname
        it[UserTable.password] = password
        it[UserTable.email] = email
    }.value