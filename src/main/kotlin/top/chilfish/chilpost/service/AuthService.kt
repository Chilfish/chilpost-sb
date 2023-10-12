package top.chilfish.chilpost.service

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.insertAndGetId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.newError
import top.chilfish.chilpost.model.*
import top.chilfish.chilpost.utils.getToken
import top.chilfish.chilpost.utils.query

@Service
@Transactional
class AuthService {
    fun userWithToken(user: UserDetails): UserToken {
        val token = getToken(TokenData(user.id, user.name))

        return UserToken(token, user.copy(password = ""))
    }

    fun login(data: AuthData): UserToken {
        val user = UserEntity
            .query("select * from user_details where email = '${data.email}' and deleted = false")
            .toUser()
            .firstOrNull() ?: throw newError(ErrorCode.NOT_FOUND_USER)

        if (user.password != data.password)
            throw newError(ErrorCode.INVALID_LOGIN)

        return userWithToken(user)
    }

    fun newUser(email: String, password: String) = UserDetails(
        id = 0,
        name = email.replace("[@.]".toRegex(), "_"),
        nickname = email.split("@").first(),
        password = password,
        email = email,
        status = UserStatus()
    )

    fun register(data: AuthData): UserToken {
        val user = newUser(data.email, data.password)
        try {
            val res = UserEntity.insertAndGetId {
                it[name] = user.name
                it[nickname] = user.nickname
                it[password] = user.password
                it[email] = user.email
            }

            return userWithToken(user.copy(id = res.value))
        } catch (e: ExposedSQLException) {
            if (e.message?.contains("Duplicate entry") == true)
                throw newError(ErrorCode.EXISTED_USER)
            throw e
        }
    }
}
