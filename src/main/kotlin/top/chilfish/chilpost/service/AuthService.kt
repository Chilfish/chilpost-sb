package top.chilfish.chilpost.service

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
}
