package top.chilfish.chilpost.model

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.json.json
import java.time.LocalDateTime

object PostTable : IntIdTable("posts") {
    val content = varchar("content", 255)
    val ownerId = integer("owner_id")
    val deleted = bool("deleted").default(false)
    val deletedAt = datetime("deleted_at").default(LocalDateTime.now())
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val isBody = bool("is_body").default(true)
    val parentId = integer("parent_id").default(-1)
}

object PostStatusT : IntIdTable("post_status") {
    val post_id = reference("post_id", PostTable.id)
    val like_count = integer("like_count").default(0)
    val comment_count = integer("comment_count").default(0)
    val repost_count = integer("repost_count").default(0)

    val likes = json<Array<String>>("likes", Json.Default).default(arrayOf())
    val comments = json<Array<String>>("comments", Json.Default).default(arrayOf())
    val reposts = json<Array<String>>("reposts", Json.Default).default(arrayOf())
}

fun List<ResultRow>.toPosts() = this.map {
    PostDetails(
        content = it[PostTable.content],
        ownerId = it[PostTable.ownerId],
        id = it[PostTable.id].value,
        isBody = it[PostTable.isBody],
        parentId = it[PostTable.parentId],
        createdAt = it[PostTable.createdAt],
//        owner = Json.decodeFromString(it[PostWithOwner.owner]),
//        status = Json.decodeFromString(it[PostWithOwner.status])
    )
}