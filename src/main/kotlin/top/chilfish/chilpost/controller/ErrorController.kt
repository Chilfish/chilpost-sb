package top.chilfish.chilpost.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.MyError
import top.chilfish.chilpost.model.ApiReturn
import top.chilfish.chilpost.utils.response

@RestController
class ErrorController {
    @RequestMapping("/error/filter")
    fun rethrow(request: HttpServletRequest): ResponseEntity<ApiReturn<Nothing?>> {
        val ex = request.getAttribute("filter.error") as Exception
        if (ex !is MyError) {
            ex.printStackTrace()
            return response(ErrorCode.UNKNOWN_ERROR, 500, ex.toString(), null)
        }

        return response(ex.code, ex.statusCode, ex.message, null)
    }
}
