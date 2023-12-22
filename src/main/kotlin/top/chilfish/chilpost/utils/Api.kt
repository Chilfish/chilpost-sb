package top.chilfish.chilpost.utils

import org.springframework.http.ResponseEntity
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.MyError
import top.chilfish.chilpost.error.newError
import top.chilfish.chilpost.model.ApiReturn

fun <T> response(
    code: ErrorCode = ErrorCode.OK,
    statusCode: Int = 200,
    message: String = "ok",
    data: T
) = ResponseEntity.ok(
    ApiReturn(code, statusCode, message, data)
)

fun responseErr(ex: MyError) = response(ex.code, ex.statusCode, ex.message, null)
fun responseErr(code: ErrorCode) = responseErr(newError(code))