package top.chilfish.chilpost.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.newError
import top.chilfish.chilpost.model.IdJson
import top.chilfish.chilpost.model.NewPost
import top.chilfish.chilpost.model.TokenData
import top.chilfish.chilpost.service.PostService
import top.chilfish.chilpost.utils.response
import java.util.*

@Controller
@RequestMapping("/api/post")
class PostController(
    private val postService: PostService
) {

    @GetMapping("/all")
    fun all(
        @RequestParam uid: String?
    ) = response(data = postService.getAll(uid))

    @GetMapping("/get")
    fun getById(
        @RequestParam id: String,
        @RequestParam uid: String?
    ) = response(data = postService.getById(id, uid))

    @GetMapping("/comments")
    fun getComments(
        @RequestParam id: String
    ) = response(data = postService.getComments(id))

    @PostMapping("/new")
    fun newPost(
        @RequestBody form: NewPost,
        @RequestAttribute("user") user: TokenData
    ): Any {
        val uid = UUID.fromString(user.id)
        val id = postService.newPost(form.content, uid, form.meta)
        if (id == -1)
            throw newError(ErrorCode.INVALID_ID)
        return response(data = mapOf("id" to id))
    }

    @PostMapping("/like")
    fun likePost(
        @RequestBody data: IdJson,
        @RequestAttribute("user") user: TokenData
    ) = response(
        data = mapOf(
            "count" to postService.likePost(data.id, user.id)
        )
    )

    @GetMapping("/test/{name}")
    fun test(
        @PathVariable name: String
    ) = response(data = postService.test(name))
}