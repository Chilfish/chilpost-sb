package top.chilfish.chilpost.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import top.chilfish.chilpost.dao.getUserId
import top.chilfish.chilpost.dao.toggleFollow
import top.chilfish.chilpost.dao.updateAvatar
import top.chilfish.chilpost.dao.updateUser
import top.chilfish.chilpost.model.UpdatedUser
import top.chilfish.chilpost.model.getUserDetail
import top.chilfish.chilpost.utils.uploadImage
import java.util.*

@Service
@Transactional
class UserService {
    fun getByName(name: String, ctxUUIDStr: String?): Map<String, Any> {
        val ctxUid = ctxUUIDStr?.let { getUserId(it) } ?: -1
        return getUserDetail(name, ctxUid)
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