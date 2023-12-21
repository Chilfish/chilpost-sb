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

    fun userHome(name: String, ctxUuidStr: String?): Any {
        val ctxUid = getUserId(ctxUuidStr)
        val user = getUserDetail(name, ctxUid)
        val posts = getPostByOwner(name).map { toPostWithOwner(it, ctxUid) }

        return mapOf(
            "owner" to user,
            "posts" to posts,
            "count" to posts.size
        )
    }

    fun follow(uid: UUID, fid: UUID) = mapOf(
        "count" to toggleFollow(uid, fid)
    )


    fun update(uid: UUID, newUser: User) = updateUser(uid, newUser)

    fun uploadAvatar(uid: UUID, avatar: MultipartFile): String {
        val avatarUrl = uploadFile(avatar, "avatars", "user_$uid.png")
        updateAvatar(uid, avatarUrl)
        return avatarUrl
    }
}