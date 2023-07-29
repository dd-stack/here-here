package com.example.Here.domain.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

@Configuration
public class EncryptConfig {

    @Value("${ENCRTYPTION_SECRET}")
    private String secret;

    @Value("${ENCRTYPTION_SECRET}")
    private String salt;

    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.text(secret, salt);
    }
}
