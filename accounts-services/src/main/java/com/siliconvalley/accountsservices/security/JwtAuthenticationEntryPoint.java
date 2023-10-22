package com.siliconvalley.accountsservices.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

//@Component
//public final class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    /**
//     * @param request       that resulted in an <code>AuthenticationException</code>
//     * @param response      so that the user agent can begin authentication
//     * @param authException that caused the invocation
//     * @throws IOException
//     * @throws ServletException
//     */
//    @Override
//    public void commence(final HttpServletRequest request,
//                         final HttpServletResponse response,
//                         final AuthenticationException authException) throws IOException {
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        final PrintWriter writer = response.getWriter();
//        writer.println(String.format("Access denied !!! %s", authException.getMessage()));
//    }
//}
