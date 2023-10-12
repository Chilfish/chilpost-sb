package top.chilfish.chilpost.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.newError
import top.chilfish.chilpost.model.ApiReturn
import top.chilfish.chilpost.model.NewPost
import top.chilfish.chilpost.model.TokenData
import top.chilfish.chilpost.service.PostService
import top.chilfish.chilpost.utils.response

@Controller
@RequestMapping("/api/post")
class PostController(
    private val postService: PostService
) {

    @GetMapping("/all")
    fun all() = response(data = postService.getAll())

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: String
    ) = response(data = postService.getById(id))

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
}