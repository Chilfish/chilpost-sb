package top.chilfish.chilpost.dao

import org.jetbrains.exposed.sql.*
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.MyError
import top.chilfish.chilpost.model.PostStatusT
import top.chilfish.chilpost.model.PostStatusT.like_count
import top.chilfish.chilpost.model.PostStatusT.likes
import top.chilfish.chilpost.model.PostStatusT.post_id
import top.chilfish.chilpost.model.PostTable
import top.chilfish.chilpost.model.PostTable.parentId
import top.chilfish.chilpost.model.UserStatusT
import top.chilfish.chilpost.model.UserTable
import top.chilfish.chilpost.utils.logger
import java.util.*

fun toPostDetail(it: ResultRow, uid: Int = -1) = mapOf(
    "id" to it[PostTable.uuid].toString(),
    "content" to it[PostTable.content],
    "created_at" to it[PostTable.createdAt],
    "is_body" to it[PostTable.isBody],

    "parent_id" to it[parentId],
    "child_id" to it[PostTable.childId],
    "owner_id" to it[PostTable.ownerId],

    "media" to it[PostTable.media],
    "status" to mapOf(
        "is_liked" to isLiked(it[PostTable.id].value, uid),
        "like_count" to it[like_count],
        "comment_count" to it[PostStatusT.comment_count],
        "repost_count" to it[PostStatusT.repost_count],
        "comments" to it[PostStatusT.comments],
        "reposts" to it[PostStatusT.reposts],
    ),
)

fun toPostWithOwner(it: ResultRow, uid: Int = -1) = toPostDetail(it, uid)
    .plus(
        mapOf(
            "owner" to mapOf(
                "id" to it[UserTable.id].value,
                "name" to it[UserTable.name],
                "nickname" to it[UserTable.nickname],
                "avatar" to it[UserTable.avatar],
            ),
        )
    )

fun postDetail() = (PostTable innerJoin PostStatusT)

fun postWithOwner() = postDetail()
    .join(
        UserTable,
        JoinType.INNER,
        PostTable.ownerId,
        UserTable.uuid
    )

fun postQuery() = postWithOwner().selectAll()
    .orderBy(PostTable.createdAt to SortOrder.DESC)

fun getAllPosts() = postQuery().andWhere { PostTable.isBody eq Op.TRUE }

fun getPostByOwner(name: String) = getAllPosts().andWhere { UserTable.name eq name }

fun getPostById(id: Int) = postQuery().andWhere { PostTable.id eq id }
fun getPostByUUId(uuid: UUID) = postQuery().andWhere { PostTable.uuid eq uuid }

fun getPostId(id: UUID) = getPostByUUId(id).firstOrNull()?.get(PostTable.id)?.value

fun getCommentsById(pcId: UUID) = postQuery()
    .andWhere { parentId eq pcId }
    .andWhere { PostTable.isBody eq Op.FALSE }

/**
 * 新增一篇帖子或是评论，返回帖子详情
 * 插入 posts 表和 post_status 表，更新 users 表的 post_count
 * 如果是评论，还要更新 parent_post 的 comment_count 和 comments
 */
fun addPost(content: String, ownerId: UUID, parentId: UUID? = null): Int {
    var parentPost: ResultRow? = null
//    logger.info("addPost: $content, $ownerId, $parentId")
    if (parentId != null) {
        parentPost = postDetail()
            .select { PostTable.uuid eq parentId }
            .firstOrNull() ?: throw MyError(ErrorCode.NOT_FOUND_POST)
    }

    val id = PostTable.insertAndGetId {
        it[PostTable.content] = content
        it[PostTable.ownerId] = ownerId
        it[uuid] = UUID.randomUUID()

        if (parentId != null) {
            it[PostTable.parentId] = parentId
            it[isBody] = false
        }
    }.value

    PostStatusT.insert { it[post_id] = id }

    // add a comment to parent post
    if (parentId != null && parentPost != null) {
        val commentsArr = parentPost[PostStatusT.comments]
            .toMutableList()
            .apply { add(id.toString()) }
            .toTypedArray()

        PostStatusT.update({ post_id eq parentPost[PostTable.id] }) {
            with(SqlExpressionBuilder) {
                it[comment_count] = comment_count + 1
                it[comments] = commentsArr
            }
        }
    }

    UserStatusT.update {
        with(SqlExpressionBuilder) {
            it[postCount] = postCount + 1
        }
    }

    return id
}

fun canComment(parentId: UUID) = postWithOwner()
    .select { PostTable.uuid eq parentId }
    .firstOrNull() != null


fun toggleLikePost(pid: Int, uid: Int): Int {
    // Retrieve the current likes of the post
    val likesArr = PostStatusT
        .select { post_id eq pid }
        .first()[likes]
        .toMutableList()

    // Initialize the variable to track the change in like count
    var added = 1

    if (likesArr.contains(uid.toString())) {
        likesArr.remove(uid.toString())
        added = -1
    } else {
        likesArr.add(uid.toString())
    }

    PostStatusT.update({ post_id eq pid }) {
        with(SqlExpressionBuilder) {
            it[like_count] = like_count + added
            it[likes] = likesArr.toTypedArray()
        }
    }

    val likes = PostStatusT.select { post_id eq pid }.first()[like_count]

    return likes
}

fun isLiked(pid: Int, uid: Int): Boolean {
    if (uid == -1) return false

    val likesArr = PostStatusT
        .select { post_id eq pid }
        .first()[likes]

    return likesArr.contains(uid.toString())
}