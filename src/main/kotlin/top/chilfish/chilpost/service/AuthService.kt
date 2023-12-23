package top.chilfish.chilpost.service

import com.google.gson.Gson
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.chilfish.chilpost.dao.addUser
import top.chilfish.chilpost.dao.getUserByEmail
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.newError
import top.chilfish.chilpost.model.*
import top.chilfish.chilpost.utils.getToken
import java.util.*

@Service
@Transactional
class AuthService {
    private val gson = Gson()

    fun userWithToken(user: User): UserToken {
        val token = getToken(TokenData(user.id, user.name))

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

        user["password"] = ""

        val u = gson.fromJson(gson.toJson(user), User::class.java)
        return userWithToken(u)
    }

    fun register(data: AuthData): UserToken {
        val name = data.email.substringBefore('@') + Date().time

        try {
            val user = User(name = name, nickname = name, email = data.email, password = data.password)
            val uuid = addUser(name, name, data.email, data.password)

            return userWithToken(user.copy(id = uuid.toString()))
        } catch (e: ExposedSQLException) {
            if (e.message?.contains("Duplicate entry") == true)
                throw newError(ErrorCode.EXISTED_USER)
            throw e
        }
    }
}
