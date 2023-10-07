package top.chilfish.chilpost.configuration
//
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.Customizer.withDefaults
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.web.SecurityFilterChain
//import top.chilfish.chilpost.filter.AuthFilter
//
//
//@Configuration
//@EnableWebSecurity
//class SecurityConfig(
//) {
//
//    @Bean
//    @Throws(Exception::class)
//    fun filterChain(http: HttpSecurity): SecurityFilterChain {
//        return http
//            .authorizeHttpRequests {
//                it
//                    .requestMatchers("/api/**")
//                    .authenticated()
//            }
//            .formLogin(withDefaults())
//            .csrf(withDefaults())
//            .logout(withDefaults())
//            .cors(withDefaults())
//            .httpBasic(withDefaults())
//            .addFilter(AuthFilter(authenticationManager))
//            .build()
//    }
//}
//
