package top.chilfish.chilpost.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import top.chilfish.chilpost.dao.*
import top.chilfish.chilpost.model.UpdatedUser
import top.chilfish.chilpost.model.User
import top.chilfish.chilpost.utils.uploadImage
import java.util.*

@Service
@Transactional
class UserService {
    fun getByName(name: String) = getUserDetail(name)

    fun userHome(name: String, ctxUuidStr: String?, page: Int, size: Int): Any {
        val ctxUid = getUserId(ctxUuidStr)
        val user = getUserDetail(name, ctxUid)
        val posts = getPostByOwner(name, page, size).map { toPostWithOwner(it, ctxUid) }

        return mapOf(
            "owner" to user,
            "posts" to posts,
            "count" to posts.size,
            "pages" to getPageCount(size),
        )
    }

    fun follow(uid: UUID, fid: UUID) = mapOf(
        "count" to toggleFollow(uid, fid)
    )

    fun update(uid: UUID, newUser: UpdatedUser) = updateUser(uid, newUser)

    fun uploadAvatar(uid: UUID, avatar: MultipartFile): String {
        val avatarUrl = uploadImage(avatar, "avatars", "user_$uid.png")
        updateAvatar(uid, avatarUrl)
        return avatarUrl
    }
}