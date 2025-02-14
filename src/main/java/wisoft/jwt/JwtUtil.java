package wisoft.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class JwtUtil {
    private static final String SECRET = "wisoft-laboratory-jwt-token-keys";

    public static String createJWT(
            final JWTPayload payload,
            final Integer expireIn
    ) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException {
        ObjectMapper objectMapper = new ObjectMapper();

        var JWTheader = new JWTHeader();
        String header = objectMapper.writeValueAsString(JWTheader);

        var now = System.currentTimeMillis();
        var exp = now + expireIn;
        var finalPayload = new JWTPayload(
                payload.getUserId(),
                payload.getUsername(),
                exp,
                now
        );

        String finalPayloadString = objectMapper.writeValueAsString(finalPayload);

        var base64Encoder = Base64.getEncoder();

        String base64Header = base64Encoder.encodeToString(header.getBytes());
        String base64Payload = base64Encoder.encodeToString(finalPayloadString.getBytes());

        String signatureAlgorithm = "HmacSHA256";
        Mac mac = Mac.getInstance(signatureAlgorithm);
        mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), signatureAlgorithm));
        var hash = mac.doFinal((base64Header + "." + base64Payload).getBytes());
        String base64Signature = base64Encoder.encodeToString(hash);

        return base64Header + "." + base64Payload + "." + base64Signature;
    }

    public static JWTPayload verifyJWT(String token) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String[] splitedToken = token.split("\\.");
        var base64Header = splitedToken[0];
        var base64Payload = splitedToken[1];
        var base64Signature = splitedToken[2];

        var base64Encoder = Base64.getEncoder();

        String signatureAlgorithm = "HmacSHA256";
        Mac mac = Mac.getInstance(signatureAlgorithm);
        mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), signatureAlgorithm));
        var hash = mac.doFinal((base64Header + "." + base64Payload).getBytes());
        String expectedSignature = base64Encoder.encodeToString(hash);

        if (!expectedSignature.equals(base64Signature)) {
            System.out.println("서명 불일치");
            return null;
        }

        var base64Decoder = Base64.getDecoder();
        var objectMapper = new ObjectMapper();
        JWTPayload payload = objectMapper.readValue(base64Decoder.decode(base64Payload), JWTPayload.class);

        var now = System.currentTimeMillis();
        var exp = payload.getExp();
        if (exp < now) {
            System.out.println("토큰 만료");
            System.out.println("exp: " + exp + "\nnow: " + now);
            return null;
        }

        return payload;
    }

    public static void main(String[] args) {
        try {
            var JWTToken = createJWT(new JWTPayload("20201914", "정예환"), 3600);

            System.out.println("JWT 토큰 발급됨 : " + JWTToken);

            var payload = verifyJWT(JWTToken);

            System.out.println("JWT 페이로드 : " + payload);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
