package be.swsb.tl.security.jwt;

import be.swsb.tl.security.CSRFConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

public class JWTCsrfTokenRepository implements CsrfTokenRepository {

    private static final String DEFAULT_CSRF_TOKEN_ATTR_NAME = CSRFConfig.class.getName()
            .concat(".CSRF_TOKEN");


    private byte[] secret;

    public JWTCsrfTokenRepository(byte[] secret) {
        this.secret = secret;
    }

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime exp = now.plusSeconds(30);

        String token;
        token = Jwts.builder()
                .setId(id)
                .setIssuedAt(asDate(now))
                .setNotBefore(asDate(now))
                .setExpiration(asDate(exp))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", token);
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        if (token == null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute(DEFAULT_CSRF_TOKEN_ATTR_NAME);
            }
        } else {
            HttpSession session = request.getSession();
            session.setAttribute(DEFAULT_CSRF_TOKEN_ATTR_NAME, token);
        }
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || HttpMethod.GET.name().equals(request.getMethod())) {
            return null;
        }
        return (CsrfToken) session.getAttribute(DEFAULT_CSRF_TOKEN_ATTR_NAME);
    }

    private Date asDate(ZonedDateTime now) {
        return Date.from(now.toInstant());
    }
}
