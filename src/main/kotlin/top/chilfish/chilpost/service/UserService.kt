package top.chilfish.chilpost.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.chilfish.chilpost.dao.getPostByOwner
import top.chilfish.chilpost.dao.getUserDetails

@Service
@Transactional
class UserService {
    fun getByName(name: String) = getUserDetails(name).firstOrNull()

    fun userHome(name: String): Map<String, Any>? {
        val user = getByName(name) ?: return null
        val posts = getPostByOwner(name)
        return mapOf(
            "owner" to user,
            "posts" to posts,
            "count" to posts.size
        )
    }

    fun test(name: String) = getUserDetails(name)
}