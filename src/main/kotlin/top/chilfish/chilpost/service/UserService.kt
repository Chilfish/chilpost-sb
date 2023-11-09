package top.chilfish.chilpost.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.chilfish.chilpost.model.PostWithOwner
import top.chilfish.chilpost.model.UserEntity
import top.chilfish.chilpost.model.toPosts
import top.chilfish.chilpost.model.toUser
import top.chilfish.chilpost.utils.logger
import top.chilfish.chilpost.utils.query

@Service
@Transactional
class UserService {
    fun getByName(name: String) = UserEntity.query(
        "Select * From user_details As u " +
                "Where u.name = '$name' " +
                "And u.deleted = False;"
    ).toUser().firstOrNull()

    fun getPostByOwner(uid: Int) = PostWithOwner.query(
        "Select * From post_with_owner As p " +
                "Where owner_id = $uid " +
                "  And is_body = True " +
                "Order By created_at Desc;"
    ).toPosts()

    fun userHome(name: String): Map<String, Any>? {
        val user = getByName(name) ?: return null
        val posts = getPostByOwner(user.id)
        return mapOf(
            "owner" to user,
            "posts" to posts,
            "count" to posts.size
        )
    }
}