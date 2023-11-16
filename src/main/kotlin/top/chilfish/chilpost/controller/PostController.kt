package top.chilfish.chilpost.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.newError
import top.chilfish.chilpost.model.*
import top.chilfish.chilpost.service.PostService
import top.chilfish.chilpost.utils.response

@Controller
@RequestMapping("/api/post")
class PostController(
    private val postService: PostService
) {

    @GetMapping("/all")
    fun all() = response(data = postService.getAll())

    @GetMapping("get")
    fun getById(
        @RequestParam id: String
    ) = response(data = postService.getById(id)[0])

    @PostMapping("/comments")
    fun getComments(
        @RequestBody data: CommentIds
    ) = response(data = postService.getComments(data.commentIds))


    @PostMapping("/new")
    fun newPost(
        @RequestBody form: NewPost,
        @RequestAttribute("user") user: TokenData
    ): ResponseEntity<ApiReturn<Int>> {
        val id = postService.newPost(form.content, user.id, form.meta)
        if (id == -1)
            throw newError(ErrorCode.INVALID_ID)
        return response(data = id)
    }

    @PostMapping("/like")
    fun likePost(
        @RequestBody data: IdJson,
        @RequestAttribute("user") user: TokenData
    ) = response(data = postService.likePost(data.id.toInt(), user.id))

    @GetMapping("/test/{name}")
    fun test(
        @PathVariable name: String
    ) = response(data = postService.test(name))
}