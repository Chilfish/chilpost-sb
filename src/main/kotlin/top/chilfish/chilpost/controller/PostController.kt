package top.chilfish.chilpost.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import top.chilfish.chilpost.PAGE_SIZE
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
        @RequestParam uid: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "$PAGE_SIZE") size: Int,
    ) = response(data = postService.getAll(uid, page, size))

    @GetMapping("/feed")
    fun feed(
        @RequestAttribute("user") user: TokenData,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "$PAGE_SIZE") size: Int,
    ) = response(data = postService.getFeed(user.id, page, size))

    @GetMapping("/get")
    fun getById(
        @RequestParam id: String,
        @RequestParam uid: String?
    ) = response(data = postService.getById(id, uid))

    @GetMapping("/comments")
    fun getComments(
        @RequestParam id: String
    ) = response(data = postService.getComments(id))

    @GetMapping("search")
    fun search(
        @RequestParam(defaultValue = "") q: String,
        @RequestParam uid: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "$PAGE_SIZE") size: Int,
    ) = response(data = postService.search(q, uid, page, size))

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
}