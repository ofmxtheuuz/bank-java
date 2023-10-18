package dev.mxtheuz.bank.application.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.mxtheuz.bank.domain.services.IJwtService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class JwtService implements IJwtService {
    private Algorithm algorithm = Algorithm.HMAC256("DSAJXOIMIAXUOSDMNXSUIAXHMAISDXMNSAUIMDXHAUI");
    private JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer("uuid")
            .build();

    @Override
    public String createToken(String uuid) {
        return JWT.create()
                .withIssuer("uuid")
                .withSubject("uuid")
                .withClaim("userUUID", uuid)
                .withIssuedAt(new Date())
                .withJWTId(UUID.randomUUID()
                        .toString())
                .withNotBefore(new Date(System.currentTimeMillis() + 1000L))
                .sign(algorithm);
    }

    @Override
    public String decodeToken(String token) {
        DecodedJWT decodedJWT = verifier.verify(token);
        Claim claim = decodedJWT.getClaim("userUUID");
        return claim.asString();
    }
}
