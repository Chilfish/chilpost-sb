package top.chilfish.chilpost.dao

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.MyError
import top.chilfish.chilpost.model.PostStatusT
import top.chilfish.chilpost.model.PostStatusT.like_count
import top.chilfish.chilpost.model.PostStatusT.likes
import top.chilfish.chilpost.model.PostStatusT.post_id
import top.chilfish.chilpost.model.PostTable
import top.chilfish.chilpost.model.PostTable.ownerId
import top.chilfish.chilpost.model.PostTable.parentId
import top.chilfish.chilpost.model.PostTable.uuid
import top.chilfish.chilpost.model.UserStatusT
import top.chilfish.chilpost.model.UserTable
import java.time.LocalDateTime
import java.util.*


fun postDetail() = (PostTable innerJoin PostStatusT)

fun postWithOwner() = postDetail()
    .join(
        UserTable,
        JoinType.INNER,
        ownerId,
        UserTable.uuid
    )

fun allPostQuery() = postWithOwner().selectAll()
    .orderBy(PostTable.createdAt to SortOrder.DESC)

fun postQuery() = allPostQuery()
    .andWhere { PostTable.deleted eq Op.FALSE }


fun getPostBody() = postQuery()
    .andWhere { PostTable.isBody eq Op.TRUE }

fun getAllPosts(page: Int, size: Int, body: Boolean = true) =
    (if (body) getPostBody() else postQuery())
        .limit(size, ((page - 1) * size).toLong())

fun getAllPostsByOwnerId(ownerUUID: UUID, page: Int, size: Int) =
    getAllPosts(page, size)
        .andWhere { ownerId eq ownerUUID }

/**
 * 获取用户关注的人以及本人的帖子
 */
fun getFeedPosts(uid: Int, page: Int, size: Int) = getAllPosts(page, size)
    .andWhere { UserTable.id inList getFollowingsList(uid).map { it.toInt() } + uid }

fun getPostByOwner(name: String, page: Int, size: Int) = getAllPosts(page, size)
    .andWhere { UserTable.name eq name }

fun getPostById(id: Int) = postQuery().andWhere { PostTable.id eq id }
fun getPostByUUId(uuid: UUID) = postQuery().andWhere { PostTable.uuid eq uuid }

fun getPostId(id: UUID) = getPostByUUId(id).firstOrNull()?.get(PostTable.id)?.value ?: -1
fun getPostId(uuidStr: String?) = if (uuidStr == null) -1 else getPostId(UUID.fromString(uuidStr))

fun getCommentsById(pcId: UUID) = postQuery()
    .andWhere { parentId eq pcId }
    .andWhere { PostTable.isBody eq Op.FALSE }

fun getPageCount(size: Int) = (getPostBody().count() / size) + 1

fun getPageCountByOwnerId(ownerUUID: UUID, size: Int) =
    (getPostBody().andWhere { ownerId eq ownerUUID }.count() / size) + 1

/**
 * 新增一篇帖子或是评论，返回帖子详情
 * 插入 posts 表和 post_status 表，更新 users 表的 post_count
 * 如果是评论，还要更新 parent_post 的 comment_count 和 comments
 */
fun addPost(content: String, ownerId: UUID, parentId: UUID? = null): Query {
    var parentPost: ResultRow? = null

    val id = transaction {
        if (parentId != null) {
            parentPost = postDetail()
                .select { uuid eq parentId }
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
            updateParentPostComment(parentPost!![PostTable.id].value, 1)
        }

        UserStatusT.update {
            with(SqlExpressionBuilder) {
                it[postCount] = postCount + 1
            }
        }

        id
    }

    val post = getPostById(id)

    return post
}

fun canComment(parentId: UUID) = postWithOwner()
    .select { uuid eq parentId }
    .firstOrNull() != null


fun toggleLikePost(pid: Int, uid: Int): Int {
    return transaction {
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

        likes
    }
}

/**
 * 删除帖子，同时更新用户的 post_count，如果是评论，还要更新 parent_post 的 comment_count
 * @param postUUID 帖子id
 * @param userUUID 用户id
 */
fun deletePost(postUUID: UUID, userUUID: UUID, parentUUID: UUID?): Boolean {
    val user = UserTable.select { UserTable.uuid eq userUUID }.firstOrNull()

    if (user == null) return false

    val uid = user[UserTable.id].value

    return transaction {
        try {
            val res1 = setDeletePost(postUUID)
            val res2 = updateUserPostCount(uid, -1)

            if (parentUUID != null) {
                val res3 = updateParentPostCommentCount(parentUUID, -1)
                res1 > 0 && res2 > 0 && res3 > 0
            } else {
                res1 > 0 && res2 > 0
            }
        } catch (e: Exception) {
            rollback()
            false
        }
    }
}

fun setDeletePost(postUUID: UUID): Int {
    return PostTable.update({ uuid eq postUUID }) {
        it[deleted] = true
        it[deletedAt] = LocalDateTime.now()
    }
}

fun updateUserPostCount(userId: Int, count: Int): Int {
    return UserStatusT.update({ UserStatusT.userId eq userId }) {
        with(SqlExpressionBuilder) {
            it[postCount] = postCount + count
        }
    }
}

fun updateParentPostCommentCount(parentUUID: UUID, count: Int): Int {
    val parentPostId = PostTable.select { uuid eq parentUUID }.first()[PostTable.id].value
    return updateParentPostComment(parentPostId, count)
}

fun updateParentPostComment(parentPostId: Int, count: Int): Int {
    return PostStatusT.update({ post_id eq parentPostId }) {
        with(SqlExpressionBuilder) {
            it[comment_count] = comment_count + count
        }
    }
}

/**
 * 判断是否点赞
 * @param pid 帖子id
 * @param uid 用户id
 */
fun isLiked(pid: Int, uid: Int): Boolean {
    if (uid == -1) return false

    val likesArr = PostStatusT
        .select { post_id eq pid }
        .first()[likes]

    return likesArr.contains(uid.toString())
}

/**
 * 搜索
 * @param keyword 关键词
 */
fun searchPosts(keyword: String, page: Int, size: Int) = getAllPosts(page, size, false)
    .andWhere { PostTable.content like "%$keyword%" }

/**
 * 获取回复的原文及用户名
 */
fun getReplyTo(postUUID: UUID): Map<String, Any>? {
    val post = PostTable.select { uuid eq postUUID }.firstOrNull() ?: return null

    val parentUUID = post[parentId] ?: return null
    val parentOwnerUUID = PostTable.select { uuid eq parentUUID }.first()[ownerId]

    val parentOwnerName = getUserByUUId(parentOwnerUUID).first()[UserTable.name]

    return mapOf(
        "id" to parentUUID,
        "uid" to parentOwnerUUID,
        "username" to parentOwnerName,
    )
}
