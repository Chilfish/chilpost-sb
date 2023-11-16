package top.chilfish.chilpost.dao

import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.newError
import top.chilfish.chilpost.model.UserEntity
import top.chilfish.chilpost.model.UserStatusT
import top.chilfish.chilpost.utils.logger

fun getUserDetails(name: String): Any {
    val res = (UserEntity innerJoin UserStatusT)
        .select { UserEntity.name eq name }
        .withDistinct()
        .map {
            mapOf(
                "id" to it[UserEntity.id].value,
                "name" to it[UserEntity.name],
                "nickname" to it[UserEntity.nickname],
                "password" to it[UserEntity.password],
                "email" to it[UserEntity.email],
                "avatar" to it[UserEntity.avatar],
                "bio" to it[UserEntity.bio],
                "deleted" to it[UserEntity.deleted],
                "createdAt" to it[UserEntity.createdAt],
                "updatedAt" to it[UserEntity.updatedAt],
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