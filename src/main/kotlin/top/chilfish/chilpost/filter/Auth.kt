package top.chilfish.chilpost.filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.annotation.WebFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import top.chilfish.chilpost.error.ErrorCode
import top.chilfish.chilpost.error.newError
import top.chilfish.chilpost.model.TokenData
import top.chilfish.chilpost.utils.verifyToken

@Order(1)
@WebFilter(filterName = "AuthFilter", urlPatterns = ["/api/*"])
class AuthFilter : Filter {
    private val whiteList = listOf(
        "/auth/.+",
        "/post/all",
        "/post/get",
        "/post/comments",
        "/post/search",
        "/user/@/.+",
        "/user/test/.+",
        "/post/test/.+",
        "/files"
    )

    override fun doFilter(
        request: ServletRequest?,
        response: ServletResponse?,
        chain: FilterChain
    ) {
        val req = request as HttpServletRequest
        val res = response as HttpServletResponse

        val path = req.requestURI.substring(req.contextPath.length).replace("/api", "")

//        logger.info("AuthFilter: $path isInWhiteList ${isInWhiteList(path)}")

        if (isInWhiteList(path))
            return chain.doFilter(request, response)

        try {
            val token = req.getHeader("Authorization")?.trim()?.split(" ")
            if (token.isNullOrEmpty() || token.size != 2 || token[0] != "Bearer")
                throw newError(ErrorCode.INVALID_TOKEN)

            val userInfo = verifyToken<TokenData>(token[1])
                ?: throw newError(ErrorCode.INVALID_TOKEN)

            req.setAttribute("user", userInfo)

            chain.doFilter(request, response)
        } catch (e: Exception) {
//            logger.info("AuthFilter: $e")
            req.setAttribute("filter.error", e)
            req.getRequestDispatcher("/err/filter").forward(req, res)
        }
    }

    private fun isInWhiteList(path: String): Boolean {
        return whiteList.any { path.matches(it.toRegex()) }
    }
}