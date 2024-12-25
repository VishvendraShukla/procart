package com.vishvendra.procart.utils.hash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("hashService")
public class HashService {

  private static final int SALT_LENGTH = 32;
  private static final int KEY_LENGTH = 32;
  private static final SecureRandom secureRandom = new SecureRandom();
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  private String generateApiKey() {
    byte[] randomBytes = new byte[KEY_LENGTH];
    secureRandom.nextBytes(randomBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
  }

  private String generateSalt() {
    byte[] salt = new byte[SALT_LENGTH];
    secureRandom.nextBytes(salt);
    return Base64.getEncoder().encodeToString(salt);
  }

  public String encodePassword(final String plainText) {
    return passwordEncoder.encode(plainText);
  }

  public boolean matchPassword(String plainText, String hashedText) {
    return passwordEncoder.matches(plainText, hashedText);
  }

  private String hashApiKey(String apiKey, String salt) {
    try {
      String saltedInput = apiKey + salt;
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(saltedInput.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(hashBytes);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("SHA-256 Algorithm not found", e);
    }
  }

  private boolean verifyApiKey(String apiKey, String hashedApiKey, String salt) {
    String hashedInput = hashApiKey(apiKey, salt);
    return hashedInput.equals(hashedApiKey);
  }

}