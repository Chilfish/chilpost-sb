package top.chilfish.chilpost.dao

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import top.chilfish.chilpost.model.PostStatusT
import top.chilfish.chilpost.model.PostTable
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

fun getPostByOwner(name: String) = postWithOwner()
    .select { UserTable.name eq name and PostTable.isBody eq Op.TRUE }
    .orderBy(PostTable.createdAt to SortOrder.DESC)
    .map(::toPostWithOwner)