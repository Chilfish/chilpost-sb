package top.chilfish.chilpost.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.chilfish.chilpost.dao.*
import top.chilfish.chilpost.model.NewPostMeta
import top.chilfish.chilpost.model.PostTable
import top.chilfish.chilpost.utils.query

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

    fun getById(id: String) =
        id.toIntOrNull()?.let {
            getPostById(it).map(::toPostWithOwner).firstOrNull()
        } ?: emptyMap()

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

        if (!canComment(ownerId, pid))
            return -1

        return addPost(content, ownerId, pid)
    }


    fun likePost(pid: Int, uid: Int) = toggleLikePost(pid, uid)

    fun test(name: String) = getPostByOwner(name)
}
