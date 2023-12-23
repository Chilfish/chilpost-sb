package top.chilfish.chilpost.controller

import kotlinx.serialization.json.Json
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
import top.chilfish.chilpost.utils.responseErr
import java.util.*

@Controller
@RequestMapping("/api/post")
class PostController(
    private val postService: PostService,
) {

    @GetMapping("/all")
    fun all(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "$PAGE_SIZE") size: Int,
        @RequestParam username: String?,
        @RequestParam with_owner: Boolean? = true,
        @RequestAttribute("ctxUser") ctxUser: TokenData? = null,
    ): Any {
        val data = if (username != null) {
            postService.getAllByOwnerName(username, page, size, ctxUser?.id)
        } else {
            postService.getAll(ctxUser?.id, page, size)
        }

        if (data == null) {
            return responseErr(ErrorCode.NOT_FOUND_USER)
        }

        // 不返回多余的 owner 信息
        if (with_owner == false) {
            data["posts"] = (data["posts"] as List<*>).map { (it as Map<*, *>).minus("owner") }
        }

        return response(data = data)
    }

    @GetMapping("/feed")
    fun feed(
        @RequestAttribute("ctxUser") ctxUser: TokenData,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "$PAGE_SIZE") size: Int,
    ) = response(data = postService.getFeed(ctxUser.id, page, size))

    @GetMapping("/get")
    fun getById(
        @RequestParam id: String,
        @RequestAttribute("ctxUser") ctxUser: TokenData?,
    ): Any {
        val data = postService.getById(id, ctxUser?.id)
            ?: return responseErr(ErrorCode.NOT_FOUND_POST)
        return response(data = data)
    }

    @GetMapping("/comments")
    fun getComments(
        @RequestParam id: String,
        @RequestAttribute("ctxUser") ctxUser: TokenData?,
    ) = response(data = postService.getComments(id, ctxUser?.id))

    @GetMapping("search")
    fun search(
        @RequestParam(defaultValue = "") q: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "$PAGE_SIZE") size: Int,
        @RequestAttribute("ctxUser") ctxUser: TokenData?,
    ) = response(data = postService.search(q, ctxUser?.id, page, size))

    @PostMapping("/new")
    fun newPost(
        @RequestBody form: NewPost,
        @RequestAttribute("ctxUser") ctxUser: TokenData,
    ): Any {
        val uid = UUID.fromString(ctxUser.id)
        val post = postService.newPost(form.content, uid, form.meta)
            ?: return responseErr(ErrorCode.INVALID_ID)
        return response(data = post)
    }

    @PostMapping("/delete")
    fun deletePost(
        @RequestBody dataStr: String,
        @RequestAttribute("ctxUser") ctxUser: TokenData,
    ): Any {
        try {
            val data = Json.decodeFromString<IdJson>(dataStr)
//            logger.info("deletePost, $data")
            return response(data = postService.rmPost(data.id, ctxUser.id, data.parent_id))
        } catch (e: Exception) {
//            logger.warn("deletePost: ${e.message}")
            val regex = Regex("Fields? (.+) .+ required")
            val match = regex.find(e.message ?: "")
            return responseErr(newError(ErrorCode.INVALID_PARAM, match?.value?.trim()))
        }
    }

    @PostMapping("/like")
    fun likePost(
        @RequestBody data: IdJson,
        @RequestAttribute("ctxUser") ctxUser: TokenData,
    ) = response(
        data = mapOf(
            "count" to postService.likePost(data.id, ctxUser.id)
        )
    )
}