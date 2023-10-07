package top.chilfish.chilpost.filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.annotation.WebFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import top.chilfish.chilpost.model.User
import top.chilfish.chilpost.utils.logger
import top.chilfish.chilpost.utils.verifyToken

@Order(1)
@WebFilter(filterName = "AuthFilter", urlPatterns = ["/*"])
class AuthFilter : Filter {
    override fun doFilter(
        request: ServletRequest?,
        response: ServletResponse?,
        chain: FilterChain?
    ) {
        val req = request as HttpServletRequest
        val res = response as HttpServletResponse
        val token = req.getHeader("Authorization")?.split(" ")?.toTypedArray()?.get(1)
        logger.info("AuthFilter: $token")

        val userInfo = verifyToken<User>(token) ?: return res.sendError(401, "Unauthorized")

        req.setAttribute("user", userInfo)
        chain!!.doFilter(request, response)
    }
}