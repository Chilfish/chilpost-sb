package top.chilfish.chilpost.dao

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import top.chilfish.chilpost.model.PostStatusT
import top.chilfish.chilpost.model.PostTable
import top.chilfish.chilpost.model.PostTable.parentId
import top.chilfish.chilpost.model.UserTable

fun toPostDetail(it: ResultRow) = mapOf(
    "id" to it[PostTable.id].value,
    "content" to it[PostTable.content],
    "created_at" to it[PostTable.createdAt],
    "is_body" to it[PostTable.isBody],
    "parent_id" to it[PostTable.parentId],
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

val postQuery = postWithOwner().selectAll()
    .orderBy(PostTable.createdAt to SortOrder.DESC)

fun getAllPosts() = postQuery.andWhere { PostTable.isBody eq Op.TRUE }

fun getPostByOwner(name: String) = getAllPosts().andWhere { UserTable.name eq name }

fun getPostById(id: Int) = getAllPosts().andWhere { PostTable.id eq id }

fun getPostByParentId(id: Int) = getAllPosts().andWhere { PostTable.parentId eq id }

fun getCommentsById(ids: List<Int>) = postQuery
    .andWhere { PostTable.id inList ids }
    .andWhere { PostTable.isBody eq Op.FALSE }

fun addPost(content: String, ownerId: Int) = PostTable.insertAndGetId {
    it[PostTable.content] = content
    it[PostTable.ownerId] = ownerId
}.value

fun canComment(uid: Int, parentId: Int) = postWithOwner()
    .select { PostTable.id eq parentId }
    .andWhere { PostTable.ownerId eq uid }
    .firstOrNull() != null

fun addComment(content: String, ownerId: Int, parentId: Int) = PostTable.insertAndGetId {
    it[PostTable.content] = content
    it[PostTable.ownerId] = ownerId
    it[PostTable.parentId] = parentId
    it[PostTable.isBody] = false
}.value