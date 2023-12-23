package top.chilfish.chilpost.model

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.json.json
import top.chilfish.chilpost.CROP_LENGTH
import top.chilfish.chilpost.dao.isLiked
import top.chilfish.chilpost.utils.cropText
import java.time.LocalDateTime

object PostTable : IntIdTable("posts") {
    val uuid = uuid("uuid").index()
    val ownerId = uuid("owner_id").index()
    val content = text("content")
    val deleted = bool("deleted").default(false)
    val deletedAt = datetime("deleted_at").nullable().default(null)
    val createdAt = datetime("created_at").default(LocalDateTime.now())

    val isBody = bool("is_body").default(true)
    val parentId = uuid("parent_id").nullable().default(null)
    val childId = uuid("child_id").nullable().default(null)

    val media = json<Array<String>>("media", Json.Default).default(arrayOf())
}

object PostStatusT : IntIdTable("post_status") {
    val post_id = reference("post_id", PostTable.id).index()

    val like_count = integer("like_count").default(0)
    val comment_count = integer("comment_count").default(0)
    val repost_count = integer("repost_count").default(0)

    val likes = json<Array<String>>("likes", Json.Default).default(arrayOf())
//    val reposts = json<Array<String>>("reposts", Json.Default).default(arrayOf())
}

fun toPostDetail(
    it: ResultRow,
    uid: Int = -1,
    crop: Boolean = true
) = mapOf(
    "id" to it[PostTable.uuid],
    "content" to if (crop) it[PostTable.content].cropText() else it[PostTable.content],
    "created_at" to it[PostTable.createdAt],
    "is_body" to it[PostTable.isBody],
    "is_long" to (it[PostTable.content].length > CROP_LENGTH),

    "parent_id" to it[PostTable.parentId],
    "child_id" to it[PostTable.childId],
    "owner_id" to it[PostTable.ownerId],
//    "deleted" to it[PostTable.deleted],

    "media" to it[PostTable.media],
    "status" to mapOf(
        "is_liked" to isLiked(it[PostTable.id].value, uid),
        "like_count" to it[PostStatusT.like_count],
        "comment_count" to it[PostStatusT.comment_count],
        "repost_count" to it[PostStatusT.repost_count],
    ),
)

fun toPostWithOwner(
    it: ResultRow,
    uid: Int = -1,
    crop: Boolean = true
) = toPostDetail(it, uid, crop).plus(
    mapOf(
        "owner" to mapOf(
            "id" to it[UserTable.uuid],
            "name" to it[UserTable.name],
            "nickname" to it[UserTable.nickname],
            "avatar" to it[UserTable.avatar],
        ),
    )
).minus("owner_id")
