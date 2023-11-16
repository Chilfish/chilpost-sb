package top.chilfish.chilpost.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.chilfish.chilpost.dao.getPostByOwner
import top.chilfish.chilpost.dao.getUserDetail
import top.chilfish.chilpost.dao.toPostWithOwner

@Service
@Transactional
class UserService {
    fun getByName(name: String) = getUserDetail(name)

    fun userHome(name: String): Map<String, Any>? {
        val user = getByName(name) ?: return null
        val posts = getPostByOwner(name).map(::toPostWithOwner)

        return mapOf(
            "owner" to user,
            "posts" to posts,
            "count" to posts.size
        )
    }

    fun test(name: String) = getUserDetail(name)
}