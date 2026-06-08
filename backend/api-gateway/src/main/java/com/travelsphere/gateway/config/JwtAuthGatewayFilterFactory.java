package com.travelsphere.gateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthGatewayFilterFactory.Config> {

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/api/v1/auth/register", "/api/v1/auth/login", "/api/v1/auth/refresh",
            "/actuator/health", "/swagger-ui", "/v3/api-docs"
    );

    private final SecretKey secretKey;
    private final ReactiveStringRedisTemplate redisTemplate;

    public JwtAuthGatewayFilterFactory(
            @Value("${app.jwt.secret:TravelSphere_JWT_Secret_Key_Min32Chars_2024}") String secret,
            ReactiveStringRedisTemplate redisTemplate) {
        super(Config.class);
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.redisTemplate = redisTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();

            // Skip public paths
            if (PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return unauthorized(exchange, "Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);
            try {
                Jws<Claims> jws = Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(token);

                Claims claims = jws.getPayload();

                // Check if token is blacklisted
                String tokenId = claims.getId();
                return redisTemplate.opsForValue().get("blacklist:" + tokenId)
                        .flatMap(blacklisted -> {
                            if (Boolean.TRUE.equals(blacklisted != null)) {
                                return unauthorized(exchange, "Token has been revoked");
                            }
                            // Add user info to headers
                            ServerWebExchange mutatedExchange = exchange.mutate()
                                    .request(r -> r.header("X-User-Id", claims.getSubject())
                                            .header("X-User-Roles", String.join(",", claims.get("roles", List.class)))
                                            .header("X-User-Email", claims.get("email", String.class)))
                                    .build();
                            return chain.filter(mutatedExchange);
                        })
                        .switchIfEmpty(Mono.defer(() -> {
                            ServerWebExchange mutatedExchange = exchange.mutate()
                                    .request(r -> r.header("X-User-Id", claims.getSubject())
                                            .header("X-User-Roles", String.join(",", claims.get("roles", List.class)))
                                            .header("X-User-Email", claims.get("email", String.class)))
                                    .build();
                            return chain.filter(mutatedExchange);
                        }));
            } catch (Exception e) {
                return unauthorized(exchange, "Invalid or expired token: " + e.getMessage());
            }
        };
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        byte[] body = ("{\"success\":false,\"error\":\"" + message + "\",\"timestamp\":\"" 
                + java.time.Instant.now() + "\"}").getBytes();
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(body)));
    }

    public static class Config {
        private boolean enabled = true;
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }
}
