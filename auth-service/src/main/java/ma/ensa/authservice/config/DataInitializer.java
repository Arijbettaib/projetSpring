package ma.ensa.authservice.config;

import ma.ensa.authservice.entity.AppRole;
import ma.ensa.authservice.entity.User;
import ma.ensa.authservice.repository.AppRoleRepo;
import ma.ensa.authservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {

    //@Bean
    CommandLineRunner initUsers(
            UserRepository userRepo,
            AppRoleRepo roleRepo,
            PasswordEncoder encoder
    ) {
        return args -> {

            AppRole userRole = roleRepo.findByName("USER")
                    .orElseGet(() -> roleRepo.save(new AppRole("USER")));

            userRepo.findByUsername("user").ifPresent(userRepo::delete);

            User user1 = new User(
                    "user",
                    encoder.encode("1234"),
                    List.of(userRole)
            );

            userRepo.save(user1);
        };
    }

    //@Bean
    CommandLineRunner initRoles(
            AppRoleRepo roleRepo
    ) {
        return args -> {

            roleRepo.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepo.save(new AppRole("USER")));
            roleRepo.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepo.save(new AppRole("ADMIN")));

        };
    }

}
