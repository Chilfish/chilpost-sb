package top.chilfish.chilpost.dao

import org.jetbrains.exposed.sql.*
import top.chilfish.chilpost.model.PostStatusT
import top.chilfish.chilpost.model.PostStatusT.likes
import top.chilfish.chilpost.model.PostStatusT.post_id
import top.chilfish.chilpost.model.PostTable
import top.chilfish.chilpost.model.PostTable.parentId
import top.chilfish.chilpost.model.UserStatusT
import top.chilfish.chilpost.model.UserTable

fun toPostDetail(it: ResultRow) = mapOf(
    "id" to it[PostTable.id].value,
    "content" to it[PostTable.content],
    "created_at" to it[PostTable.createdAt],
    "is_body" to it[PostTable.isBody],
    "parent_id" to it[parentId],
    "owner_id" to it[PostTable.ownerId],
    "status" to mapOf(
        "like_count" to it[PostStatusT.like_count],
        "comment_count" to it[PostStatusT.comment_count],
        "repost_count" to it[PostStatusT.repost_count],
        "likes" to it[PostStatusT.likes],
        "comments" to it[PostStatusT.comments],
        "reposts" to it[PostStatusT.reposts],
    ),
)

fun toPostWithOwner(it: ResultRow) = toPostDetail(it).plus(
    mapOf(
        "owner" to mapOf(
            "id" to it[UserTable.id].value,
            "name" to it[UserTable.name],
            "nickname" to it[UserTable.nickname],
            "avatar" to it[UserTable.avatar],
        ),
    )
)


fun postWithOwner() = PostTable
    .innerJoin(PostStatusT)
    .join(
        UserTable,
        JoinType.INNER,
        PostTable.ownerId,
        UserTable.id
    )

fun postQuery() = postWithOwner().selectAll()
    .orderBy(PostTable.createdAt to SortOrder.DESC)

fun getAllPosts() = postQuery().andWhere { PostTable.isBody eq Op.TRUE }

fun getPostByOwner(name: String) = getAllPosts().andWhere { UserTable.name eq name }

fun getPostById(id: Int) = postQuery().andWhere { PostTable.id eq id }

fun getCommentsById(pcId: Int) = postQuery()
    .andWhere { parentId eq pcId }
    .andWhere { PostTable.isBody eq Op.FALSE }

// add a pure post or a comment
fun addPost(content: String, ownerId: Int, parentId: Int? = null): Int {
    val id = PostTable.insertAndGetId {
        it[PostTable.content] = content
        it[PostTable.ownerId] = ownerId

        if (parentId != null) {
            it[PostTable.parentId] = parentId
            it[isBody] = false
        }
    }.value

    PostStatusT.insert { it[post_id] = id }

    // add a comment to parent post
    if (parentId != null) {
        val commentsArr = PostStatusT
            .select { post_id eq parentId }
            .first()[PostStatusT.comments]
            .toMutableList()
            .apply { add(id) }
            .toIntArray()

        PostStatusT.update({ post_id eq parentId }) {
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

fun canComment(uid: Int, parentId: Int) = postWithOwner()
    .select { PostTable.id eq parentId }
    .andWhere { PostTable.ownerId eq uid }
    .firstOrNull() != null


fun toggleLikePost(pid: Int, uid: Int): Int {
    val likesArr = PostStatusT
        .select { post_id eq pid }
        .first()[likes]
        .toMutableList()

    var added = 1

    if (likesArr.contains(uid)) {
        likesArr.remove(uid)
        added = -1
    } else {
        likesArr.add(uid)
    }

    val rows = PostStatusT.update({ post_id eq pid }) {
        with(SqlExpressionBuilder) {
            it[like_count] = like_count + added
            it[likes] = likesArr.toIntArray()
        }
    }

    return rows
}