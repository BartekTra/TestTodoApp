package com.example.demo.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.example.demo.dao.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class    JwtUtils {

    private String SECRET_KEY = "secret";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        try {
            // Parse the claims from the token
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("Token has expired");
            throw new IllegalStateException("Token has expired", e);
        } catch (io.jsonwebtoken.SignatureException e) {
            System.out.println("Invalid token signature");
            throw new IllegalArgumentException("Invalid token signature", e);
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            System.out.println("Malformed token");
            throw new IllegalArgumentException("Malformed token", e);
        } catch (io.jsonwebtoken.MissingClaimException | io.jsonwebtoken.IncorrectClaimException e) {
            System.out.println("Not enough claims or incorrect claims");
            throw new IllegalArgumentException("Not enough claims or incorrect claims in the token", e);
        } catch (Exception e) {
            System.out.println("Unknown error while extracting claims");
            throw new IllegalArgumentException("An error occurred while extracting claims", e);
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, User userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
