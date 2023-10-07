package top.chilfish.chilpost.utils

import org.springframework.http.ResponseEntity
import top.chilfish.chilpost.model.ApiReturn

fun <T> response(
    code: String = "ok",
    statusCode: Int = 200,
    message: String = "ok",
    data: T
) = ResponseEntity.ok(
    ApiReturn(code, statusCode, message, data)
)