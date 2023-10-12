package top.chilfish.chilpost.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import top.chilfish.chilpost.model.ApiReturn
import top.chilfish.chilpost.model.TokenData
import top.chilfish.chilpost.service.PostService
import top.chilfish.chilpost.utils.logger
import top.chilfish.chilpost.utils.response

@Controller
@RequestMapping("/api/post")
class PostController(
    private val postService: PostService
) {

    @GetMapping("/all")
    fun all() = response(data = postService.getAll())

    @GetMapping("/test")
    fun get(
        @RequestAttribute("user") user: TokenData
    ): ResponseEntity<ApiReturn<TokenData>> {
        logger.info("PostController: $user")
        return response(data = user)
    }

    data class NewPost(val content: String, val owner_id: String)

    @PostMapping("/new")
    fun newPost(@RequestBody form: NewPost) {
        return postService.newPost(form.content, form.owner_id)
    }
}