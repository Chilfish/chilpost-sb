package top.chilfish.chilpost.model

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.json.json
import top.chilfish.chilpost.model.UserTable.default
import java.time.LocalDateTime
import java.util.*

object PostTable : IntIdTable("posts") {
    val uuid  = uuid("uuid").index()
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
    val comments = json<Array<String>>("comments", Json.Default).default(arrayOf())
    val reposts = json<Array<String>>("reposts", Json.Default).default(arrayOf())
}
