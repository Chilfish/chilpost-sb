package top.chilfish.chilpost.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.chilfish.chilpost.model.*
import top.chilfish.chilpost.utils.getToken
import top.chilfish.chilpost.utils.logger
import top.chilfish.chilpost.utils.query

@Service
@Transactional
class AuthService {
    fun userWithToken(user: UserDetails): UserToken {
        val token = getToken(TokenData(user.id, user.name))

        logger.info("AuthService: $token")
        return UserToken(token, user.copy(password = ""))
    }

    fun login(data: AuthData): UserToken {
        val user = UserEntity
            .query("select * from user_details where email = '${data.email}' and deleted = false")
            .toUser()
            .firstOrNull()

        logger.info("AuthService: $user")

        if (user == null || user.password != data.password)
            throw Exception("Invalid email or password") // @TODO: use custom exception

        return userWithToken(user)
    }
}
