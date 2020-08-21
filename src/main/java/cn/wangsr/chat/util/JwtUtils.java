package cn.wangsr.chat.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.UUID;

/**
 * @author wjl
 */
public class JwtUtils {
    private static Logger log = LoggerFactory.getLogger(JwtUtils.class);

    public static final String AUTH_HEADER_KEY = "Authorization";
    private static final Long EXPIRE = 24 * 3600 * 1000L;
    private static final String SECRET = "wangsr";

    public static String createJwt(String username,Long userId){
        Assert.notNull(username,"用户名不能为空");
        Assert.notNull(userId,"用户ID不能为空");
        String token = Jwts.builder().setSubject(userId.toString())
                .claim("userId",userId)
                .claim("username",username)
                .setIssuedAt(new Date())
                .setId(UUID.randomUUID().toString())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE))
                .signWith(SignatureAlgorithm.HS256,SECRET).compact();
        return token;
    }

    public static Claims parseJwt(String token){
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    public static void main(String[] args) {
//        System.out.println(createJwt("aaa",8989));
        String aa = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIOTg5IiwidXNlcklkIjo4OTg5LCJ1c2VybmFtZSI6ImFhYSIsImlhdCI6MTU5NzkwMTgwMSwiZXhwIjoxNTk3OTg4MjAxfQ.VRr9Vki15Cl2SAf7JGxzAvqnognfu-BifE00QikWwmI";
        System.out.println(parseJwt(aa));
    }


}
