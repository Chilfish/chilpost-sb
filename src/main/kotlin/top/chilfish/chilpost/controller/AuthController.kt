package top.chilfish.chilpost.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import top.chilfish.chilpost.model.ApiReturn
import top.chilfish.chilpost.model.AuthData
import top.chilfish.chilpost.model.UserToken
import top.chilfish.chilpost.service.AuthService
import top.chilfish.chilpost.utils.response

@Controller
@RequestMapping("/api/auth")
class AuthController(
    val authService: AuthService
) {

    @RequestMapping("/login")
    fun login(
        @RequestBody form: AuthData
    ): ResponseEntity<ApiReturn<UserToken>> {
        val data = authService.login(form)
        return response(data = data)
    }
}