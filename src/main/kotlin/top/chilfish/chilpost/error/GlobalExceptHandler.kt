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
//        logger.error("GlobalExceptionHandler: $ex")
        val err = ex as MyError
        return response(err.code, err.statusCode, err.message, null)
    }
}
