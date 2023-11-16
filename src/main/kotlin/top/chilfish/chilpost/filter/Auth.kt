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
import top.chilfish.chilpost.utils.logger
import top.chilfish.chilpost.utils.verifyToken

@Order(1)
@WebFilter(filterName = "AuthFilter", urlPatterns = ["/api/*"])
class AuthFilter : Filter {
    private val whiteList = listOf(
        "/auth/.+",
        "/post/all",
        "/post/get",
        "/post/comments",
        "/user/@/.+",
        "/user/test/.+",
        "/post/test/.+"
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

        val token = req.getHeader("Authorization")?.split(" ")?.toTypedArray()?.get(1)
//        logger.info("AuthFilter: $token")

        try {
            val userInfo = verifyToken<TokenData>(token)
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