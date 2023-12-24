package top.chilfish.chilpost.dao

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.newError
import top.chilfish.chilpost.model.UpdatedUser
import top.chilfish.chilpost.model.UserStatusT
import top.chilfish.chilpost.model.UserStatusT.followers
import top.chilfish.chilpost.model.UserStatusT.followings
import top.chilfish.chilpost.model.UserTable
import java.util.*

fun userDetail() = (UserTable innerJoin UserStatusT)

fun getUserById(id: Int) = userDetail().select { UserTable.id eq id }
fun getUserByUUId(uuid: UUID) = userDetail().select { UserTable.uuid eq uuid }
fun getUserByEmail(email: String) = userDetail().select { UserTable.email eq email }
fun getUserByName(name: String) = userDetail().select { UserTable.name eq name }

fun getUserUUID(name: String) = UserTable
    .select { UserTable.name eq name }
    .firstOrNull()?.get(UserTable.uuid)

fun getUserId(uuid: UUID) = getUserByUUId(uuid).firstOrNull()?.get(UserStatusT.userId)?.value ?: -1
fun getUserId(uuidStr: String?) = if (uuidStr == null) -1 else getUserId(UUID.fromString(uuidStr))

fun getFollowersList(uid: Int) = UserStatusT.select { UserStatusT.userId eq uid }.first()[followers].toList()
fun getFollowingsList(uid: Int) = UserStatusT.select { UserStatusT.userId eq uid }.first()[followings].toList()


fun addUser(name: String, nickname: String, email: String, password: String): Query {
    return transaction {
        val id = UserTable
            .insertAndGetId {
                it[UserTable.name] = name
                it[UserTable.nickname] = nickname
                it[UserTable.password] = password
                it[UserTable.email] = email
                it[uuid] = UUID.randomUUID()
            }.value

        UserStatusT.insert { it[userId] = id }

        val user = getUserById(id)
        user
    }
}

/**
 * 关注或取消关注
 * @param uidUU 用户id
 * @param fidUU 对方id
 * @return 操作完成后的对方的粉丝数
 */
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

    transaction {
        try {
            UserStatusT.update({ UserStatusT.userId eq uid.toInt() }) {
                it[followings] = myFollowings.toTypedArray()
                it[followingCount] = myFollowings.size
            }

            UserStatusT.update({ UserStatusT.userId eq fid.toInt() }) {
                it[followers] = otherFollowers.toTypedArray()
                it[followerCount] = otherFollowers.size
            }
        } catch (e: Exception) {
            rollback()
            false
        }
    }

    return otherFollowers.size
}

/**
 * 判断是否关注
 * @param uid 用户id
 * @param fid 对方id
 * @return 是否关注
 */
fun isFollowing(uid: Int, fid: Int): Boolean {
    if (uid == -1 || fid == -1) return false

    val me = UserStatusT.select { UserStatusT.userId eq uid }.firstOrNull()
        ?: throw newError(ErrorCode.NOT_FOUND_USER)
    val other = UserStatusT.select { UserStatusT.userId eq fid }.firstOrNull()
        ?: throw newError(ErrorCode.NOT_FOUND_USER)

    val myFollowings = me[followings].toList() // 我的关注列表
    val otherFollowers = other[followers].toList() // 对方的粉丝列表

    return myFollowings.contains(fid.toString()) && otherFollowers.contains(uid.toString())
}

fun updateUser(uid: UUID, newUser: UpdatedUser) =
    UserTable.update({ UserTable.uuid eq uid }) {
        it[name] = newUser.name
        it[nickname] = newUser.nickname
        it[email] = newUser.email
        it[bio] = newUser.bio

        // TODO: admin only
        // it[level] = newUser.level
    }

fun updateAvatar(uid: UUID, avatar: String) =
    UserTable.update({ UserTable.uuid eq uid }) {
        it[UserTable.avatar] = avatar
    }
