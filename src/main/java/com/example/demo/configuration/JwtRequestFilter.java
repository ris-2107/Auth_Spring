

package com.example.demo.configuration;

import com.example.demo.service.UserDetailsServiceImpl;
import com.example.demo.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter
{

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private UserDetailsServiceImpl jwtService;

    @Override
    protected void doFilterInternal (
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException
    {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith(
            "Bearer "))
        {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(jwtToken);
            }
            catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            }
            catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        }
        else {
            if (requestTokenHeader != null) {
                logger.error("JWT Token does not begin with Bearer String");
            }
            else {
                logger.error("JWT Token is null");
            }
            // System.out.println("JWT token does not start with Bearer");
            //logger.error("JWT Token does not begin with Bearer String");
        }

        if (username != null
            && SecurityContextHolder.getContext().getAuthentication() == null)
        {

            UserDetails userDetails = jwtService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken
                    usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                    request));
                SecurityContextHolder.getContext()
                                     .setAuthentication(
                                         usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);

    }

}