package top.chilfish.chilpost.controller

import kotlinx.serialization.json.Json
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.newError
import top.chilfish.chilpost.model.TokenData
import top.chilfish.chilpost.model.User
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
    ) = response(data = userService.userHome(name))


    @PostMapping("/follow")
    fun follow(
        @RequestAttribute("user") user: TokenData,
        @RequestBody data: Map<String, Int>
    ): Any {
        val uid = data["id"] ?: throw newError(ErrorCode.INVALID_PARAM)
        return response(data = userService.follow(user.id, uid))
    }

    @PostMapping("/update")
    fun update(
        @RequestAttribute("user") user: TokenData,
        @RequestBody data: String
    ): Any {
        try {
            val newUser = Json.decodeFromString<User>(data)
            return response(data = userService.update(user.id, newUser))
        } catch (e: Exception) {
            throw newError(ErrorCode.INVALID_PARAM)
        }
    }

    @PostMapping("/update/avatar")
    fun updateAvatar(
        @RequestAttribute("user") user: TokenData,
        @RequestParam("avatar") avatar: MultipartFile?
    ): Any {
        if (avatar == null || avatar.isEmpty)
            throw newError(ErrorCode.INVALID_PARAM)

        return response(data = userService.updateAvatar(user.id, avatar))
    }
}