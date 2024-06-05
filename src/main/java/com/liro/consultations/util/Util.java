package main.java.com.liro.consultations.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import main.java.com.liro.consultations.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Component
public class Util {
    public static <T> void updateIfNotNull(Consumer<T> setterMethod, T value) {
        if (value != null){
            setterMethod.accept(value);
        }
    }
    public static UserDTO getUser(String token){
        Claims claims;

        claims = Jwts.parser()
                //  .setSigningKey("codigo_secreto".getBytes())
                .setSigningKey(Base64.getEncoder().encodeToString("asdfAEGVDSAkdnASBOIAW912927171Q23Q".getBytes()))
                .parseClaimsJws(token.substring(7))
                .getBody();

        return  UserDTO.builder()
                .email((String) claims.get("email"))
                .id(Long.valueOf((Integer) claims.get("id")))
                .roles((List<String>) claims.get("authorities"))
                .build();
    }
}
