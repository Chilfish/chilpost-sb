package top.chilfish.chilpost.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.chilfish.chilpost.dao.*
import top.chilfish.chilpost.model.NewPostMeta
import java.util.UUID

@Service
@Transactional
class PostService {
    fun getAll(): Map<String, Any> {
        val posts = getAllPosts().map(::toPostWithOwner)

        return mapOf(
            "posts" to posts,
            "count" to posts.size
        )
    }

    fun getById(id: String): MutableMap<String, Any>? {
        val uuid = UUID.fromString(id)

        val post = getPostByUUId(uuid).map(::toPostWithOwner)
            .firstOrNull()?.toMutableMap()
            ?: return null

        if (post["parent_id"] != -1) {
            val parent = getPostById(post["parent_id"] as Int)
                .map(::toPostWithOwner).firstOrNull() ?: return null

            post["parent_post"] = parent
        }

        return post
    }

    fun getComments(pcId: String): Map<String, Any> {
        val comments = pcId.toIntOrNull()?.let {
            getCommentsById(it).map(::toPostWithOwner)
        } ?: emptyList()

        return mapOf(
            "comments" to comments,
            "count" to comments.size
        )
    }

    fun newPost(content: String, ownerId: Int, meta: NewPostMeta): Int {
//        logger.info("newPost: $content, $ownerId, $meta")

        if (meta.type == "comment")
            return newComment(content, ownerId, meta.pcId)

        if (meta.type != "post")
            return -1

        return addPost(content, ownerId)
    }

    fun newComment(content: String, ownerId: Int, parentId: String?): Int {
        val pid = parentId?.toIntOrNull() ?: return -1

        if (!canComment(pid))
            return -1

        return addPost(content, ownerId, pid)
    }


    fun likePost(pid: String, uid: String): Int {
        val postId = pid.toIntOrNull()
        val userId = uid.toIntOrNull()

        return if (postId != null && userId != null) {
            toggleLikePost(postId, userId)
        } else {
            -1
        }
    }

    fun test(name: String) = getPostByOwner(name)
}
