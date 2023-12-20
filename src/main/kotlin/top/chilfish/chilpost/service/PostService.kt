package top.chilfish.chilpost.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.chilfish.chilpost.dao.*
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.newError
import top.chilfish.chilpost.model.NewPostMeta
import top.chilfish.chilpost.model.UserStatusT.userId
import java.util.UUID

@Service
@Transactional
class PostService {
    fun getAll(uid: String?): Map<String, Any> {
        val userId = getUserId(uid)
        val posts = getAllPosts().map { toPostWithOwner(it, userId) }

        return mapOf(
            "posts" to posts,
            "count" to posts.size
        )
    }

    fun getById(id: String, uid: String?): MutableMap<String, Any?>? {
        val uuid = UUID.fromString(id)
        val userId = getUserId(uid)

        val post = getPostByUUId(uuid).map { toPostWithOwner(it, userId) }
            .firstOrNull()?.toMutableMap()
            ?: return null

        if (post["parent_id"] != null) {
            val parent = getPostByUUId(post["parent_id"] as UUID)
                .map { toPostWithOwner(it, userId) }.firstOrNull() ?: return null

            post["parent_post"] = parent
        }

        return post
    }

    fun getComments(pcId: String): Map<String, Any> {
        val uuid = UUID.fromString(pcId)

        val comments = getCommentsById(uuid).map(::toPostWithOwner)


        return mapOf(
            "comments" to comments,
            "count" to comments.size
        )
    }

    fun newPost(content: String, ownerId: UUID, meta: NewPostMeta): Int {
//        logger.info("newPost: $content, $ownerId, $meta")

        if (meta.type == "comment" && meta.pcId != null)
            return newComment(content, ownerId, meta.pcId)

        if (meta.type != "post")
            return -1

        return addPost(content, ownerId)
    }

    fun newComment(content: String, ownerId: UUID, parentId: UUID): Int {
        if (!canComment(parentId))
            return -1

        return addPost(content, ownerId, parentId)
    }


    fun likePost(pid: String, uid: String): Int {
        val postId = getPostId(UUID.fromString(pid))
        val userId = getUserId(UUID.fromString(uid))

        if (postId == null || userId == null)
            throw newError(ErrorCode.INVALID_PARAM)

        return toggleLikePost(postId, userId)
    }

    fun test(name: String) = getPostByOwner(name)
}
