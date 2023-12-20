package top.chilfish.chilpost.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import top.chilfish.chilpost.dao.*
import top.chilfish.chilpost.model.User
import top.chilfish.chilpost.utils.uploadFile
import java.util.UUID

@Service
@Transactional
class UserService {
    fun getByName(name: String) = getUserDetail(name)

    fun userHome(name: String, uid: String?): Any {
        val user = getByName(name)
        val userId = getUserId(uid)
        val posts = getPostByOwner(name).map { toPostWithOwner(it, userId) }

        return mapOf(
            "owner" to user,
            "posts" to posts,
            "count" to posts.size
        )
    }

    fun follow(uid: UUID, fid: UUID) = toggleFollow(uid, fid)

    fun update(uid: UUID, newUser: User) = updateUser(uid, newUser)

    fun uploadAvatar(uid: UUID, avatar: MultipartFile): String {
        val avatarUrl = uploadFile(avatar, "avatars", "user_$uid.png")
        updateAvatar(uid, avatarUrl)
        return avatarUrl
    }
}