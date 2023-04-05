package com.solbeg.wallet.security;

import com.solbeg.wallet.dto.UserDto;
import com.solbeg.wallet.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    @Value("${token.secret}")
    private String key;

    private static final String BEARER = "Bearer ";

    private final UserService userService;

    public Authentication getAuthentication(String rawToken) {

        Claims body = validateAndGetBody(rawToken);
        String userName = body.getSubject();
        UserDto userDetails = userService.loadUserByUsername(userName);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String generateToken(UserDetails user) {

        return BEARER + Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.UTC)))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    private Claims validateAndGetBody(String token) {

        try {
            if (Strings.isNotBlank(token)) {
                String tokenWithoutPrefix = token.substring(BEARER.length());
                Claims body = Jwts.parser()
                        .setSigningKey(DatatypeConverter.parseBase64Binary(key))
                        .parseClaimsJws(tokenWithoutPrefix)
                        .getBody();
                if (isNotExpired(body.getExpiration())) {
                    return body;
                }
            }
        } catch (JwtException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        throw new BadCredentialsException("Invalid token");
    }

    private static boolean isNotExpired(Date expiration) {

        return expiration.after(new Date());
    }
}
