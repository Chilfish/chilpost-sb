package top.chilfish.chilpost.error

enum class ErrorCode {
    OK,
    UNKNOWN_ERROR,
    NOT_FOUND_USER,
    NOT_FOUND_POST,
    NOT_FOUND_COMMENT,

    INVALID_PARAM,
    INVALID_TOKEN,
    INVALID_LOGIN,
    INVALID_ID,

    EXISTED_USER,
}

data class MyError(
    val code: ErrorCode = ErrorCode.UNKNOWN_ERROR,
    val statusCode: Int = 500,
    override val message: String = "Internal Server Error",
) : Exception()

val Errors = listOf(
    MyError(ErrorCode.NOT_FOUND_USER, 404, "User not found"),
    MyError(ErrorCode.NOT_FOUND_POST, 404, "Post not found"),
    MyError(ErrorCode.NOT_FOUND_COMMENT, 404, "Comment not found"),

    MyError(ErrorCode.INVALID_PARAM, 400, "Invalid param"),
    MyError(ErrorCode.INVALID_TOKEN, 403, "Invalid token"),
    MyError(ErrorCode.INVALID_LOGIN, 403, "Invalid password or email"),

    MyError(ErrorCode.INVALID_ID, 400, "Invalid id"),
    MyError(ErrorCode.EXISTED_USER, 409, "User existed"),
)


fun newError(code: ErrorCode): MyError {
    return Errors.firstOrNull { it.code == code } ?: MyError()
}