package com.donts.blog.role;

import com.donts.exception.BlogBizException;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class ArticleAccessTokenGenerator {
    private static final String LOG_PREFIX = "ArticleAccessTokenGenerator";

    public static String generateToken(String articleId, String articlePassword, String loginToken) {
        String input = articleId + articlePassword + loginToken;
        return getSHA256(input);
    }

    private static String getSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("{} Could not generate token", LOG_PREFIX, e);
            throw new BlogBizException("Could not generate token", e);
        }
    }
}