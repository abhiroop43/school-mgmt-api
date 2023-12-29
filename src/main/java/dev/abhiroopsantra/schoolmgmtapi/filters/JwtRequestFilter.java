package dev.abhiroopsantra.schoolmgmtapi.filters;

import dev.abhiroopsantra.schoolmgmtapi.services.jwt.UserDetailsServiceImpl;
import dev.abhiroopsantra.schoolmgmtapi.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil                jwtUtil;

    public JwtRequestFilter(
            UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil
                           ) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil            = jwtUtil;
    }

    @Override protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
                                             ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token      = null;
        String username   = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token    = authHeader.substring(7);
            username = jwtUtil.extractUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token, userDetails)) {

                Collection<SimpleGrantedAuthority> oldAuthorities
                        = (Collection<SimpleGrantedAuthority>) userDetails.getAuthorities();

                String role = (String) jwtUtil.extractAllClaims(token).get("role");

                // add the role to the existing authorities
                SimpleGrantedAuthority       authority          = new SimpleGrantedAuthority(role);
                List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<>();
                updatedAuthorities.add(authority);
                updatedAuthorities.addAll(oldAuthorities);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, updatedAuthorities);

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
