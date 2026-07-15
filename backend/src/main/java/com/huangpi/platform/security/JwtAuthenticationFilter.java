package com.huangpi.platform.security;

import com.huangpi.platform.domain.AccountStatus;
import com.huangpi.platform.repository.RevokedTokenRepository;
import com.huangpi.platform.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RevokedTokenRepository revokedTokenRepository;
    private final UserRepository userRepository;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            RevokedTokenRepository revokedTokenRepository,
            UserRepository userRepository,
            RestAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtService = jwtService;
        this.revokedTokenRepository = revokedTokenRepository;
        this.userRepository = userRepository;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7).trim();
        try {
            SessionPrincipal principal = jwtService.parse(token);
            if (revokedTokenRepository.existsByJti(principal.jti())) {
                throw new IllegalArgumentException("Token revoked");
            }
            var user = userRepository.findById(principal.userId()).orElseThrow();
            if (user.getStatus() != AccountStatus.ENABLED || user.getRole() != principal.role()) {
                throw new IllegalArgumentException("Account disabled or role changed");
            }
            var authentication = new UsernamePasswordAuthenticationToken(
                    principal,
                    token,
                    List.of(new SimpleGrantedAuthority("ROLE_" + principal.role().name())));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, new InvalidTokenAuthenticationException(exception));
        }
    }

    private static class InvalidTokenAuthenticationException extends org.springframework.security.core.AuthenticationException {
        InvalidTokenAuthenticationException(Throwable cause) {
            super("Invalid token", cause);
        }
    }
}
