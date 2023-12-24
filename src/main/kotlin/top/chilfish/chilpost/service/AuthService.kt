package top.chilfish.chilpost.service

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.chilfish.chilpost.dao.addUser
import top.chilfish.chilpost.dao.getUserByEmail
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.newError
import top.chilfish.chilpost.model.AuthData
import top.chilfish.chilpost.model.TokenData
import top.chilfish.chilpost.model.UserToken
import top.chilfish.chilpost.model.toUserDetail
import top.chilfish.chilpost.utils.getToken
import java.util.*

@Service
@Transactional
class AuthService {

    fun userWithToken(user: Map<String, Any>): UserToken {
        val token = getToken(TokenData(user["id"] as String, user["name"] as String))

        return UserToken(token, user)
    }

    fun login(data: AuthData): UserToken {
        val user = (
                getUserByEmail(data.email)
                    .map(::toUserDetail)
                    .firstOrNull() ?: throw newError(ErrorCode.NOT_FOUND_USER)
                ).toMutableMap()

        if (user["password"] != data.password)
            throw newError(ErrorCode.INVALID_LOGIN)

        return userWithToken(user.minus("password"))
    }

    fun register(data: AuthData): UserToken {
        val name = data.email.substringBefore('@') + Date().time

        try {
            val user = addUser(name, name, data.email, data.password)
                .map { toUserDetail(it) }.first()
                .minus("password")

            return userWithToken(user)
        } catch (e: ExposedSQLException) {
            if (e.message?.contains("Duplicate entry") == true)
                throw newError(ErrorCode.EXISTED_USER)
            throw e
        }
    }
}
