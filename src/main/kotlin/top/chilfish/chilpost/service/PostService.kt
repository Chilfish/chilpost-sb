package top.chilfish.chilpost.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.chilfish.chilpost.dao.*
import top.chilfish.chilpost.model.NewPostMeta
import top.chilfish.chilpost.model.PostTable
import top.chilfish.chilpost.utils.logger
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

    fun getById(id: String) = getPostById(id.toInt()).map(::toPostWithOwner).firstOrNull()

    fun getComments(ids: List<Int>): Map<String, Any> {
        val comments = getCommentsById(ids).map(::toPostWithOwner)

        return mapOf(
            "comments" to comments,
            "count" to comments.size
        )
    }

    fun newPost(content: String, ownerId: Int, meta: NewPostMeta): Int {
        logger.info("newPost: $content, $ownerId, $meta")

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

        return addComment(content, ownerId, pid)
    }


    fun likePost(pid: Int, uid: Int): Any {
        PostTable.query(
            "Update post_status\n" +
                    "Set like_count = like_count + 1,\n" +
                    "    likes      = Json_Array_Append(likes, '\$', $uid)\n" +
                    "Where post_id = $pid;"
        )

        return 1
    }

    fun test(name: String) = getPostByOwner(name)
}
