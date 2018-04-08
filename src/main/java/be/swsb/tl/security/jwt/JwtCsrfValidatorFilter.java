package be.swsb.tl.security.jwt;

import be.swsb.tl.security.SecretService;
import be.swsb.tl.security.WebSecurityConfig;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * This filter is supposed to be configured to run AFTER @see{CsrfFilter} in WebSecurityConfig
 */
@Component
public class JwtCsrfValidatorFilter extends OncePerRequestFilter {

    @Autowired
    private SecretService secretService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // A real implementation should have a nonce cache so the token cannot be reused
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");

        if (isPOST(request) && notAnIgnoredPath(request) && token != null) {
            // CsrfFilter already made sure the token matched.
            // Here, we'll make sure it's not expired
            try {
                Jwts.parser()
                        .setSigningKeyResolver(secretService.getSigningKeyResolver())
                        .parseClaimsJws(token.getToken());
            } catch (JwtException e) {
                request.setAttribute("exception", e);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                RequestDispatcher dispatcher = request.getRequestDispatcher("expired-jwt");
                dispatcher.forward(request, response);
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean notAnIgnoredPath(HttpServletRequest request) {
        return Arrays.binarySearch(WebSecurityConfig.ignoreCsrfAntMatchers, request.getServletPath()) < 0;
    }

    private boolean isPOST(HttpServletRequest request) {
        return HttpMethod.POST.name().equals(request.getMethod());
    }
}
