package com.template.az.SecurityTemplate.auth.security;

import com.template.az.SecurityTemplate.auth.dto.UserWithAccount;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${jwt.expirationTime}")
    private long JWT_TOKEN_VALIDITY;

    @Value("${jwt.secret}")
    private String secret;

    static final String USER_TYPE = "userType";
    static final String USER_UUID = "userUuid";
    static final String ACCOUNT_UUID = "accountUuid";
    static final String USER_LOGIN = "userLogin";
    static final String USER_EMAIL = "userEmail";
    static final String ACCOUNT_ENABLED = "accountEnabled";
    static final String ACCOUNT_LOCKED = "accountLocked";
    static final String PASSWORD_LAST_CHANGE_TIME = "passwordLastTimeChanged";

    //retrieve username from jwt token
    public String getUsernameFromToken(final String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(final String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    public Claims getAllClaimsFromToken(final String token) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    private @NotNull SecretKey getSecretKey() {
        final byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //check if the token has expired
    public Boolean isTokenExpired(final String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(final UserWithAccount user) {
        final Map<String, Object> extraClaims = Map.of(
                USER_TYPE, user.roles(),
                USER_LOGIN, user.username(),
                ACCOUNT_UUID, user.accountUuid(),
                USER_UUID, user.userUuid(),
                USER_EMAIL, user.email(),
                ACCOUNT_ENABLED, user.isEnabled(),
                ACCOUNT_LOCKED, user.isNonLocked(),
                PASSWORD_LAST_CHANGE_TIME, user.passwordLastTimeChanged().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );

        return doGenerateToken(extraClaims, user);
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(final Map<String, Object> claims, final UserWithAccount user) {
        return Jwts.builder()
                .claims(claims)
                .subject(user.username())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(getSecretKey())
                .compact();
    }

    //validate token
    public Boolean validateToken(final String token, final UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
