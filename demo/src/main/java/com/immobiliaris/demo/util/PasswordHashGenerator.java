package com.immobiliaris.demo.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class per generare password hash BCrypt
 * Esegui questa classe per generare gli hash da inserire nel database
 */
public class PasswordHashGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Genera hash per le password di esempio
        String adminPassword = "adminpass";
        String agentPassword = "agentpass";
        
        String adminHash = encoder.encode(adminPassword);
        String agentHash = encoder.encode(agentPassword);
        
        System.out.println("=== Password Hash Generator ===");
        System.out.println();
        System.out.println("Admin password: " + adminPassword);
        System.out.println("Admin hash: " + adminHash);
        System.out.println();
        System.out.println("Agent password: " + agentPassword);
        System.out.println("Agent hash: " + agentHash);
        System.out.println();
        System.out.println("=== SQL INSERT Statements ===");
        System.out.println("INSERT INTO users (email, password, role, enabled) VALUES");
        System.out.println("('admin@example.com', '" + adminHash + "', 'ADMIN', TRUE),");
        System.out.println("('agent@example.com', '" + agentHash + "', 'AGENT', TRUE);");
    }
}
