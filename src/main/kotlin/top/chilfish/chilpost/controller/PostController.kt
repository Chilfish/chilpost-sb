package top.chilfish.chilpost.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import top.chilfish.chilpost.model.PostDetails
import top.chilfish.chilpost.model.User
import top.chilfish.chilpost.service.PostService
import top.chilfish.chilpost.utils.logger

@Controller
@RequestMapping("/api/post")
class PostController(
    private val postService: PostService
) {

    @GetMapping("/all")
    fun all(
        @RequestAttribute("user") user: User
    ): ResponseEntity<List<PostDetails>> {
        val res = postService.getAll()
        logger.info("req attr: $res")
        return ResponseEntity.ok(res)
    }

    data class NewPost(val content: String, val owner_id: String)

    @PostMapping("/new")
    fun newPost(@RequestBody form: NewPost) {
        return postService.newPost(form.content, form.owner_id)
    }
}