package com.example.accountsservices.security;

import com.example.accountsservices.helpers.JwtHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class JwtAuthenticationFiler extends OncePerRequestFilter {
    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;

    JwtAuthenticationFiler(final JwtHelper jwtHelper,
                           final UserDetailsService userDetailsService){
        this.jwtHelper=jwtHelper;
        this.userDetailsService=userDetailsService;
    }

    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request,final HttpServletResponse response,final FilterChain filterChain) throws ServletException, IOException {
        final String requestHeader = request.getHeader("Authorization");
        String userName = null;
        String jwtToken = null;

        if (null != requestHeader && requestHeader.startsWith("Bearer")) {
            //Bearer 6969696969696
            jwtToken = requestHeader.substring(7);
            try {
                userName = jwtHelper.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException | ExpiredJwtException | MalformedJwtException e) {
                e.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else {
            log.warn("Invalid Jwt Token");
        }

        if (StringUtils.isNotEmpty(userName) &&
                Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            //fetch user details from token
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            final Boolean validateToken = jwtHelper.validateToken(jwtToken, userDetails);
            if (validateToken) {
                //set the auth to context holder
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken
                                (userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }else{
                log.warn("Ouuch!! Validation failed");
            }
        }
        filterChain.doFilter(request,response);
    }
}
