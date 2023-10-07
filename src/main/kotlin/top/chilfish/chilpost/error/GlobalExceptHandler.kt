package top.chilfish.chilpost.error

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.ModelAndView
import top.chilfish.chilpost.utils.logger

@Component
class GlobalExceptionHandler : HandlerExceptionResolver {

    override fun resolveException(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any?,
        ex: Exception
    ): ModelAndView? {
        // 处理异常逻辑
        val errorMessage = "发生了一个错误：${ex.message}"

        logger.error(errorMessage, ex)
        response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write(errorMessage)
        response.writer.flush()
        return ModelAndView()
    }
}
//@ControllerAdvice
//class GlobalExceptHandler :
//    BasicErrorController(DefaultErrorAttributes(), ErrorProperties()) {
//
//    @ExceptionHandler(Exception::class)
//    fun handleException(ex: Exception): ResponseEntity<ApiReturn<Nothing?>> {
//        val err = ex as MyError
//        logger.error("GlobalExceptHandler1: $err")
//        return response(err.code, err.statusCode, err.message, null)
//    }
//
//
//     override fun error(request: HttpServletRequest): ResponseEntity<MutableMap<String, Any?>> {
//        val err = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL))
//
//        logger.info("GlobalExceptHandler2: $err")
//        return ResponseEntity.ok(
//            err
//        )
//    }
//}
