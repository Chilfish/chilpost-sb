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
import top.chilfish.chilpost.model.User
import top.chilfish.chilpost.model.UserToken
import top.chilfish.chilpost.utils.getToken

@Service
@Transactional
class AuthService {
    fun userWithToken(user: User): UserToken {
        val token = getToken(TokenData(user.id, user.name))

        return UserToken(token, user.copy(password = ""))
    }

    fun login(data: AuthData): UserToken {
        val user = getUserByEmail(data.email) ?: throw newError(ErrorCode.NOT_FOUND_USER)

        if (user.password != data.password)
            throw newError(ErrorCode.INVALID_LOGIN)

        return userWithToken(user)
    }

    fun register(data: AuthData): UserToken {
        val name = data.email.substringBefore('@')

        try {
            val user = User(name = name, nickname = name, email = data.email, password = data.password)
            val id = addUser(name, name, data.email, data.password)

            return userWithToken(user.copy(id = id))
        } catch (e: ExposedSQLException) {
            if (e.message?.contains("Duplicate entry") == true)
                throw newError(ErrorCode.EXISTED_USER)
            throw e
        }
    }
}
