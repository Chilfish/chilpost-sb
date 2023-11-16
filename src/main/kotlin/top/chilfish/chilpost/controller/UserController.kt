package top.chilfish.chilpost.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.newError
import top.chilfish.chilpost.model.TokenData
import top.chilfish.chilpost.service.UserService
import top.chilfish.chilpost.utils.response

@Controller
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/me")
    fun me(
        @RequestAttribute("user") user: TokenData
    ) = response(data = userService.getByName(user.name))

    @GetMapping("/@/{name}")
    fun userHome(
        @PathVariable name: String
    ): Any {
        val data = userService.userHome(name)
            ?: throw newError(ErrorCode.NOT_FOUND_USER)
        return response(data = data)
    }

    @GetMapping("/test/{name}")
    fun test(
        @PathVariable name: String
    ) = response(data = userService.test((name)))
}