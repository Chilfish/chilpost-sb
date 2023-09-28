package top.chilfish.chilpost.common

data class ApiReturn<T : Any>(
    val data: T,
    val code: String = "ok",
    val message: String = "ok",
    val statusCode: Number = 200
)