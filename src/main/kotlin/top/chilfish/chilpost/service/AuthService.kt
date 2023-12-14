package top.chilfish.chilpost.service

import com.google.gson.Gson
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.chilfish.chilpost.dao.addUser
import top.chilfish.chilpost.dao.getUserByEmail
import top.chilfish.chilpost.dao.toUserDetail
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
        val token = getToken(TokenData(user.uuid, user.name))

        return UserToken(token, user.copy(password = "******", id = user.uuid))
    }

    fun login(data: AuthData): UserToken {
        val user = (getUserByEmail(data.email)
            .map(::toUserDetail)
            .firstOrNull() ?: throw newError(ErrorCode.NOT_FOUND_USER)).toMutableMap()

        if (user["password"] != data.password)
            throw newError(ErrorCode.INVALID_LOGIN)

        user["uuid"] = user["id"] as Any
        user["id"] = 0

        val gson = Gson()

//        logger.info(gson.toJson(user))
        val u = gson.fromJson(gson.toJson(user), User::class.java)

        return userWithToken(u)
    }

    fun register(data: AuthData): UserToken {
        val name = data.email.substringBefore('@')

        try {
            val user = User(name = name, nickname = name, email = data.email, password = data.password)
            val uid = addUser(name, name, data.email, data.password)

            return userWithToken(user.copy(uuid = uid.toString()))
        } catch (e: ExposedSQLException) {
            if (e.message?.contains("Duplicate entry") == true)
                throw newError(ErrorCode.EXISTED_USER)
            throw e
        }
    }
}
