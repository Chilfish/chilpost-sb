package top.chilfish.chilpost.service

import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.statements.UpdateStatement
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.chilfish.chilpost.model.NewPostMeta
import top.chilfish.chilpost.model.PostStatusT
import top.chilfish.chilpost.model.PostWithOwner
import top.chilfish.chilpost.model.toPosts
import top.chilfish.chilpost.utils.logger
import top.chilfish.chilpost.utils.query

@Service
@Transactional
class PostService {
    private val table = "post_with_owner"

    fun getAll(): Map<String, Any> {
        val posts = PostWithOwner.query("select * from $table where is_body is true;").toPosts()

        return mapOf(
            "posts" to posts,
            "count" to posts.size
        )
    }

    fun getById(id: String) =
        PostWithOwner.query("select * from $table where id = $id Order By created_at Desc").toPosts()

    fun newPost(content: String, ownerId: Int, meta: NewPostMeta): Int {
        logger.info("newPost: $content, $ownerId, $meta")

        if (meta.type == "comment")
            return newComment(content, ownerId, meta.pcId)

        if (meta.type != "post")
            return -1

        val id = PostWithOwner.insertAndGetId {
            it[PostWithOwner.content] = content
            it[PostWithOwner.ownerId] = ownerId
        }

        return id.value
    }

    fun newComment(content: String, ownerId: Int, parentId: String?): Int {
        logger.info("newComment: $content, $ownerId, $parentId")
        if (parentId == null)
            return -1

        PostWithOwner.query(
            "select * from users where id = $ownerId " +
                    "and exists (select 1 from posts where id = $parentId);"
        ).firstOrNull() ?: return -1

        val id = PostWithOwner.insertAndGetId {
            it[PostWithOwner.content] = content
            it[PostWithOwner.ownerId] = ownerId
            it[PostWithOwner.parentId] = parentId.toInt()
            it[isBody] = false
        }

        return id.value
    }

    fun getComments(ids: List<Int>): Map<String, Any> {
        val comments = PostWithOwner.query(
            "select * from $table " +
                    "where id in (${ids.joinToString()}) and is_body = false Order By created_at Desc"
        ).toPosts()

        return mapOf(
            "comments" to comments,
            "count" to comments.size
        )
    }

    fun likePost(pid: Int, uid: Int): Any {
        PostWithOwner.query(
            "Update post_status\n" +
                    "Set like_count = like_count + 1,\n" +
                    "    likes      = Json_Array_Append(likes, '\$', $uid)\n" +
                    "Where post_id = $pid;"
        )

        return 1
    }
}
