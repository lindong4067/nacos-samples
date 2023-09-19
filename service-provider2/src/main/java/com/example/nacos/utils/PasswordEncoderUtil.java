package com.example.nacos.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * used for generate default password for nacos
 */
public class PasswordEncoderUtil {
    public static void main(String[] args) {

        /**
         * INSERT INTO users (username, password, enabled) VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', TRUE);
         * INSERT INTO roles (username, role) VALUES ('nacos', 'ROLE_ADMIN');
         */
        System.out.println(new BCryptPasswordEncoder().encode("nacos"));
    }
}
