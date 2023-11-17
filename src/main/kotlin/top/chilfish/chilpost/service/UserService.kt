package top.chilfish.chilpost.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.chilfish.chilpost.dao.*
import top.chilfish.chilpost.model.User

@Service
@Transactional
class UserService {
    fun getByName(name: String) = getUserDetail(name)

    fun userHome(name: String): Any {
        val user = getByName(name)
        val posts = getPostByOwner(name).map(::toPostWithOwner)

        return mapOf(
            "owner" to user,
            "posts" to posts,
            "count" to posts.size
        )
    }

    fun follow(uid: Int, fid: Int) = toggleFollow(uid.toString(), fid.toString())

    fun update(uid: Int, newUser: User) = updateUser(uid, newUser)
}