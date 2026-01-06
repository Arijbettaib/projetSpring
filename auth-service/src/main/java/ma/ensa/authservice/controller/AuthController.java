package ma.ensa.authservice.controller;

import ma.ensa.authservice.entity.User;
import ma.ensa.authservice.repository.UserRepository;
import ma.ensa.authservice.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ma.ensa.authservice.entity.AppRole;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * LOGIN ENDPOINT
     * POST /auth/login
     */
    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {

        // 1️⃣ Chercher l'utilisateur
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️⃣ Vérifier le mot de passe (bcrypt)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 3️⃣ Générer le JWT
        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRoles()
                        .stream()
                        .map(AppRole::getName)
                        .toList()
        );

        // 4️⃣ Retourner le token
        return new TokenResponse(token);
    }

    // ===================== DTOs =====================

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class TokenResponse {
        private String token;

        public TokenResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }
}
