package top.chilfish.chilpost.model

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime

object UserEntity : IntIdTable("users") {
    val name = varchar("name", 255)
    val nickname = varchar("nickname", 255)
    val password = varchar("password", 255)
    val avatar = varchar("avatar", 255).default("/placeholder.avatar.png")
    val bio = varchar("bio", 255).default("hello")
    val email = varchar("email", 255)

    val createdAt = datetime("created_at")
    val deleted = bool("deleted").default(false)
    val level = varchar("level", 255).default("user")

    val status = text("status")
}


fun List<ResultRow>.toUser() = this.map {
    UserDetails(
        id = it[UserEntity.id].value,
        name = it[UserEntity.name],
        nickname = it[UserEntity.nickname],
        password = it[UserEntity.password],
        email = it[UserEntity.email],
        avatar = it[UserEntity.avatar],
        bio = it[UserEntity.bio],
        level = it[UserEntity.level],
        deleted = it[UserEntity.deleted],
        createdAt = it[UserEntity.createdAt],
        status = Json.decodeFromString(it[UserEntity.status])
    )
}