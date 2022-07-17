package com.datasolution.msa.microservice1.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class SigningKeyResolver extends SigningKeyResolverAdapter {
    @Override
    public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims) {
        /* KEY ID 가 설정되어 있지 않으면 에러 발생 */
        String keyId = header.getKeyId();
        log.info("keyId = {}", keyId);

        if(ObjectUtils.isEmpty(keyId)) {
            return null;
        }

        String jwtKey = encryptionSHA256(keyId);

        return jwtKey.getBytes();
    }

    /**
     * 문자열을 SHA256 알고리즘으로 암호화한다.
     *
     * @param text
     * @return
     */
    public String encryptionSHA256(String text) {
        String shaText = "";

        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(text.getBytes());
            byte[] byteData = sh.digest();
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }

            shaText = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            shaText = null;
        }

        return shaText;
    }
}
