package top.chilfish.chilpost.controller

import kotlinx.serialization.json.Json
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import top.chilfish.chilpost.PAGE_SIZE
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.newError
import top.chilfish.chilpost.model.TokenData
import top.chilfish.chilpost.model.UpdatedUser
import top.chilfish.chilpost.model.User
import top.chilfish.chilpost.service.UserService
import top.chilfish.chilpost.utils.response
import java.util.*

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
        @PathVariable name: String,
        @RequestParam uid: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "$PAGE_SIZE") size: Int,
    ) = response(data = userService.userHome(name, uid, page, size))


    @PostMapping("/follow")
    fun follow(
        @RequestAttribute("user") user: TokenData,
        @RequestBody data: Map<String, String>
    ): Any {
        try {
            val fid = UUID.fromString(data["id"])
            val uid = UUID.fromString(user.id)

            return response(data = userService.follow(uid, fid))

        } catch (e: Exception) {
            throw newError(ErrorCode.INVALID_PARAM)
        }
    }

    @PostMapping("/update")
    fun update(
        @RequestAttribute("user") user: TokenData,
        @RequestBody data: String
    ): Any {
        try {
            val newUser = Json.decodeFromString<UpdatedUser>(data)
            val uid = UUID.fromString(user.id)
            return response(data = userService.update(uid, newUser))
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

        val uid = UUID.fromString(user.id)
        return response(data = userService.uploadAvatar(uid, avatar))
    }
}