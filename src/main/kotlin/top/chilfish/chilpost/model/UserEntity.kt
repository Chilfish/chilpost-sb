package top.chilfish.chilpost.model

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object UserEntity : IntIdTable("_user") {
    val name = varchar("name", 255)
    val nickname = varchar("nickname", 255)
    val password = varchar("password", 255)
    val email = varchar("email", 255)
    val avatar = varchar("avatar", 255).default("/placeholder.avatar.png")
    val bio = varchar("bio", 255).default("hello")
    val level = varchar("level", 255).default("user")

    val deleted = bool("deleted").default(false)
    val deletedAt = datetime("deleted_at")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}

// views
