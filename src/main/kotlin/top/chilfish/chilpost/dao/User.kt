package top.chilfish.chilpost.dao

import org.jetbrains.exposed.sql.*
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.newError
import top.chilfish.chilpost.model.User
import top.chilfish.chilpost.model.UserStatusT
import top.chilfish.chilpost.model.UserStatusT.followers
import top.chilfish.chilpost.model.UserStatusT.followings
import top.chilfish.chilpost.model.UserTable
import java.util.*

fun toUserDetail(it: ResultRow) = mapOf(
    "id" to it[UserTable.uuid].toString(),
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
        "followers" to it[followers],
        "followings" to it[followings],
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

fun userDetail() = (UserTable innerJoin UserStatusT)

fun getUserById(id: Int) = userDetail().select { UserTable.id eq id }
fun getUserByUUId(uuid: UUID) = userDetail().select { UserTable.uuid eq uuid }
fun getUserByEmail(email: String) = userDetail().select { UserTable.email eq email }

fun getUserId(id: UUID) = getUserByUUId(id).firstOrNull()?.get(UserStatusT.userId)?.value

fun addUser(name: String, nickname: String, email: String, password: String): UUID {
    val id = UserTable
        .insertAndGetId {
            it[UserTable.name] = name
            it[UserTable.nickname] = nickname
            it[UserTable.password] = password
            it[UserTable.email] = email
            it[uuid] = UUID.randomUUID()
        }.value

    UserStatusT.insert { it[userId] = id }

    val uuid = getUserById(id).first()[UserTable.uuid]
    return uuid
}

fun toggleFollow(uidUU: UUID, fidUU: UUID): Int {
    val me = userDetail().select { UserTable.uuid eq uidUU }.first()

    val other = userDetail().select { UserTable.uuid eq fidUU }.firstOrNull()
        ?: throw newError(ErrorCode.NOT_FOUND_USER)

    val uid = me[UserTable.id].value.toString()
    val fid = other[UserTable.id].value.toString()

    val myFollowings = me[followings].toMutableList() // 我的关注列表
    val otherFollowers = other[followers].toMutableList() // 对方的粉丝列表

    if (myFollowings.contains(fid)) {
        myFollowings.remove(fid)
        otherFollowers.remove(uid)
    } else {
        myFollowings.add(fid)
        otherFollowers.add(uid)
    }

    val res1 = UserStatusT.update({ UserStatusT.userId eq uid.toInt() }) {
        it[followings] = myFollowings.toTypedArray()
        it[followingCount] = myFollowings.size
    }

    val res2 = UserStatusT.update({ UserStatusT.userId eq fid.toInt() }) {
        it[followers] = otherFollowers.toTypedArray()
        it[followerCount] = otherFollowers.size
    }

    return res1 + res2
}

fun updateUser(uid: UUID, newUser: User) =
    UserTable.update({ UserTable.uuid eq uid }) {
        it[name] = newUser.name
        it[nickname] = newUser.nickname
        it[email] = newUser.email
        it[avatar] = newUser.avatar
        it[bio] = newUser.bio

        // TODO: admin only
        // it[level] = newUser.level
    }

fun updateAvatar(uid: UUID, avatar: String) =
    UserTable.update({ UserTable.uuid eq uid }) {
        it[UserTable.avatar] = avatar
    }
