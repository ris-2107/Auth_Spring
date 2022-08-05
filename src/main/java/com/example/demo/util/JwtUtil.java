 
package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

@Component
public class JwtUtil
{

    private static final String SECRET_KEY = generateRandomString(10);

    public static String generateRandomString (int length)
    {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private <T> T getClaimFromToken (
        String token, Function<Claims, T> claimResolver)
    {
        final Claims claims = getAllClaimsFromToken(token);
        return claimResolver.apply(claims);

    }

    private Claims getAllClaimsFromToken (String token)
    {
        return Jwts.parser()
                   .setSigningKey(SECRET_KEY)
                   .parseClaimsJws(token)
                   .getBody();
    }

    public String getUsernameFromToken (String token)
    {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public boolean validateToken (String token, UserDetails userDetails)
    {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(
            token));
    }

    private boolean isTokenExpired (String token)
    {
        final Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    private Date getExpirationDateFromToken (String token)
    {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String generateToken (UserDetails userDetails)
    {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                   .setClaims(claims)
                   .setSubject(userDetails.getUsername())
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(
                       System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                   .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                   .compact();
    }
}
