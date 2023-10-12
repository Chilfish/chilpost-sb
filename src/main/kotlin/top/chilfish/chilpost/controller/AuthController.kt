package top.chilfish.chilpost.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import top.chilfish.chilpost.model.AuthData
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
    ) = response(data = authService.login(form))

    @RequestMapping("/register")
    fun register(
        @RequestBody form: AuthData
    ) = response(data = authService.register(form))
}