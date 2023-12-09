package top.chilfish.chilpost.error

import org.springframework.boot.autoconfigure.web.ErrorProperties
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import top.chilfish.chilpost.model.ApiReturn
import top.chilfish.chilpost.utils.response

@ControllerAdvice
class GlobalExceptionHandler :
    BasicErrorController(DefaultErrorAttributes(), ErrorProperties()) {

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ApiReturn<Nothing?>> {
        if (ex !is MyError) {
            ex.printStackTrace()
            return response(ErrorCode.UNKNOWN_ERROR, 500, ex.toString(), null)
        }
        return response(ex.code, ex.statusCode, ex.message, null)
    }
}
