package top.chilfish.chilpost.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.chilfish.chilpost.dao.*
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.newError
import top.chilfish.chilpost.model.NewPostMeta
import top.chilfish.chilpost.model.PostTable
import top.chilfish.chilpost.model.toPostWithOwner
import java.util.*

@Service
@Transactional
class PostService {
    /**
     * 广场，获取所有动态
     */
    fun getAll(uid: String?, page: Int, size: Int): Map<String, Any> {
        val userId = getUserId(uid)
        val posts = getAllPosts(page, size).map { toPostWithOwner(it, userId) }
        val pages = getPageCount(size)

        return mapOf(
            "posts" to posts,
            "count" to posts.size,
            "pages" to pages,
            "page_size" to size,
        )
    }

    /**
     * 获取用户关注的人的动态
     * @param uid 用户id
     * @param page 页码
     * @param size 每页数量
     */
    fun getFeed(uid: String, page: Int, size: Int): Map<String, Any> {
        val userId = getUserId(uid)
        val posts = getFeedPosts(userId, page, size).map { toPostWithOwner(it, userId) }
        val pages = getPageCount(size)

        return mapOf(
            "posts" to posts,
            "count" to posts.size,
            "pages" to pages,
            "page_size" to size,
        )
    }

    /**
     * 获取推文详情
     * @param pid 推文UUID
     * @param uid 用户UUID
     */
    fun getById(pid: String, uid: String?): Any? {
        val postUUID = UUID.fromString(pid)
        val userId = getUserId(uid)

        val post = getPostByUUId(postUUID).map { toPostWithOwner(it, userId, crop = false) }
            .firstOrNull()?.toMutableMap()
            ?: return null

        if (post["parent_id"] != null) {
            val parent = getPostByUUId(post["parent_id"] as UUID)
                .map { toPostWithOwner(it, userId) }.firstOrNull()

            post["parent_post"] = parent
        }

        return mapOf(
            "post" to post,
        )
    }

    /**
     * 获取评论
     * @param pcId 父评论id
     */
    fun getComments(pcId: String, uid: String?): Map<String, Any> {
        val postUUID = UUID.fromString(pcId)
        val ctxUid = getUserId(uid)

        val comments = getCommentsById(postUUID).map { toPostWithOwner(it, ctxUid) }

        return mapOf(
            "posts" to comments,
            "count" to comments.size,
        )
    }

    /**
     * 搜索
     * @param keyword 关键词
     * @param uid 用户id
     */
    fun search(keyword: String, uid: String?, page: Int, size: Int): Map<String, Any> {
        if (keyword.isEmpty()) {
            return mapOf(
                "posts" to listOf<Map<String, Any>>(),
                "count" to 0,
                "page_size" to size,
            )
        }

        val userId = getUserId(uid)
        val posts = searchPosts(keyword, page, size).map {
            val replyTo = getReplyTo(it[PostTable.uuid])
            toPostWithOwner(it, userId).toMutableMap().apply {
                this["reply_to"] = replyTo
            }
        }

        return mapOf(
            "posts" to posts,
            "count" to posts.size,
            "page_size" to size,
        )
    }

    /**
     * 新增一篇帖子或是评论，返回帖子详情
     * 插入 posts 表和 post_status 表，更新 users 表的 post_count
     * 如果是评论，还要更新 parent_post 的 comment_count 和 comments
     */
    fun newPost(content: String, userUUID: UUID, meta: NewPostMeta): Any? {
//        logger.info("newPost: $content, $ownerId, $meta")

        if (meta.type == "comment" && meta.pcId != null)
            return newComment(content, userUUID, meta.pcId)

        if (meta.type != "post")
            return null

        val userId = getUserId(userUUID)

        val post = addPost(content, userUUID).map { toPostWithOwner(it, userId) }.first()

        return mapOf(
            "post" to post,
        )
    }

    /**
     * 新增一篇帖子
     */
    fun newComment(content: String, userUUID: UUID, parentId: UUID): Any? {
        if (!canComment(parentId))
            return null

        val userId = getUserId(userUUID)

        val post = addPost(content, userUUID, parentId).map { toPostWithOwner(it, userId) }.first()

        return mapOf(
            "post" to post,
        )
    }

    fun rmPost(pid: String, uid: String, parentId: String?): Boolean {
        try {
            val postUUID = UUID.fromString(pid)
            val userUUID = UUID.fromString(uid)
            val parentUUID = parentId?.let { UUID.fromString(it) }

            return deletePost(postUUID, userUUID, parentUUID)
        } catch (e: Exception) {
            return false
        }
    }

    /**
     * 点赞或取消点赞
     * @param pid 帖子id
     * @param uid 用户id
     */
    fun likePost(pid: String, uid: String): Int {
        val postId = getPostId(UUID.fromString(pid))
        val userId = getUserId(UUID.fromString(uid))

        if (postId == -1 || userId == -1)
            throw newError(ErrorCode.INVALID_PARAM)

        return toggleLikePost(postId, userId)
    }
}
