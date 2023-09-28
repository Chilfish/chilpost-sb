package top.chilfish.chilpost.model

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object PostWithOwner : IntIdTable("posts") {
    val content = varchar("content", 255)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val isBody = bool("is_body").default(true)
    val parentId = integer("parent_id").default(-1)
    val ownerId = integer("owner_id")

    // should be json
    val status = text("status")
    val owner = text("owner")
}

fun List<ResultRow>.toPosts() = this.map {
    PostDetails(
        content = it[PostWithOwner.content],
        ownerId = it[PostWithOwner.ownerId],
        id = it[PostWithOwner.id].value,
        isBody = it[PostWithOwner.isBody],
        parentId = it[PostWithOwner.parentId],
        createdAt = it[PostWithOwner.createdAt],
        owner = Json.decodeFromString(it[PostWithOwner.owner]),
        status = Json.decodeFromString(it[PostWithOwner.status])
    )
}