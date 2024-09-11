package com.example.demo.securityconfig;

import com.example.demo.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService,HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
   try {
       String token = authHeader.substring(7);
       String username = jwtService.extractUsername(token);

       if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

           UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);


           if (jwtService.isValid(token, userDetails)) {
               UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                       userDetails, null, userDetails.getAuthorities()
               );

               authToken.setDetails(
                       new WebAuthenticationDetailsSource().buildDetails(request)
               );

               SecurityContextHolder.getContext().setAuthentication(authToken);
           }
       }
       filterChain.doFilter(request, response);
   } catch (Exception exception) {
       System.out.println("Filter exception.........");
       handlerExceptionResolver.resolveException(request, response, null, exception);
   }
    }
}