package top.chilfish.chilpost.service

import org.jetbrains.exposed.sql.insertAndGetId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.chilfish.chilpost.dao.getPostByOwner
import top.chilfish.chilpost.dao.postWithOwner
import top.chilfish.chilpost.model.NewPostMeta
import top.chilfish.chilpost.model.PostTable
import top.chilfish.chilpost.model.toPosts
import top.chilfish.chilpost.utils.logger
import top.chilfish.chilpost.utils.query

@Service
@Transactional
class PostService {
    private val table = "post_with_owner"

    fun getAll(): Map<String, Any> {
        val posts = PostTable.query("select * from $table where is_body is true;").toPosts()

        return mapOf(
            "posts" to posts,
            "count" to posts.size
        )
    }

    fun getById(id: String) =
        PostTable.query("select * from $table where id = $id Order By created_at Desc").toPosts()

    fun newPost(content: String, ownerId: Int, meta: NewPostMeta): Int {
        logger.info("newPost: $content, $ownerId, $meta")

        if (meta.type == "comment")
            return newComment(content, ownerId, meta.pcId)

        if (meta.type != "post")
            return -1

        val id = PostTable.insertAndGetId {
            it[PostTable.content] = content
            it[PostTable.ownerId] = ownerId
        }

        return id.value
    }

    fun newComment(content: String, ownerId: Int, parentId: String?): Int {
        logger.info("newComment: $content, $ownerId, $parentId")
        if (parentId == null)
            return -1

        PostTable.query(
            "select * from users where id = $ownerId " +
                    "and exists (select 1 from posts where id = $parentId);"
        ).firstOrNull() ?: return -1

        val id = PostTable.insertAndGetId {
            it[PostTable.content] = content
            it[PostTable.ownerId] = ownerId
            it[PostTable.parentId] = parentId.toInt()
            it[isBody] = false
        }

        return id.value
    }

    fun getComments(ids: List<Int>): Map<String, Any> {
        val comments = PostTable.query(
            "select * from $table " +
                    "where id in (${ids.joinToString()}) and is_body = false Order By created_at Desc"
        ).toPosts()

        return mapOf(
            "comments" to comments,
            "count" to comments.size
        )
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
