package top.chilfish.chilpost.controller

import kotlinx.serialization.json.Json
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.newError
import top.chilfish.chilpost.model.TokenData
import top.chilfish.chilpost.model.UpdatedUser
import top.chilfish.chilpost.service.UserService
import top.chilfish.chilpost.utils.response
import java.util.*

@Controller
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/me")
    fun me(
        @RequestAttribute("ctxUser") ctxUser: TokenData,
    ) = response(data = userService.getByName(ctxUser.name))

    @GetMapping("/profile/{name}")
    fun userHome(
        @PathVariable name: String,
    ) = response(data = userService.getByName(name).minus("email"))


    @PostMapping("/follow")
    fun follow(
        @RequestAttribute("ctxUser") ctxUser: TokenData,
        @RequestBody data: Map<String, String>,
    ): Any {
        try {
            val fid = UUID.fromString(data["id"])
            val uid = UUID.fromString(ctxUser.id)

            return response(data = userService.follow(uid, fid))

        } catch (e: Exception) {
            throw newError(ErrorCode.INVALID_PARAM)
        }
    }

    @PostMapping("/update")
    fun update(
        @RequestAttribute("ctxUser") ctxUser: TokenData,
        @RequestBody data: String,
    ): Any {
        try {
            val newUser = Json.decodeFromString<UpdatedUser>(data)
            val uid = UUID.fromString(ctxUser.id)
            return response(data = userService.update(uid, newUser))
        } catch (e: Exception) {
            throw newError(ErrorCode.INVALID_PARAM)
        }
    }

    @PostMapping("/update/avatar")
    fun updateAvatar(
        @RequestAttribute("ctxUser") ctxUser: TokenData,
        @RequestParam("avatar") avatar: MultipartFile?,
    ): Any {
        if (avatar == null || avatar.isEmpty)
            throw newError(ErrorCode.INVALID_PARAM)

        val uid = UUID.fromString(ctxUser.id)
        return response(data = userService.uploadAvatar(uid, avatar))
    }
}