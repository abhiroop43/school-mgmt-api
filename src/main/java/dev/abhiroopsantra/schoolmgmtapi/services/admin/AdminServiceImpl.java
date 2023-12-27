package dev.abhiroopsantra.schoolmgmtapi.services.admin;

import dev.abhiroopsantra.schoolmgmtapi.entities.User;
import dev.abhiroopsantra.schoolmgmtapi.enums.UserRole;
import dev.abhiroopsantra.schoolmgmtapi.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl {

    private final Environment env;
    private final UserRepository userRepository;

    public AdminServiceImpl(UserRepository userRepository, Environment env) {
        this.userRepository = userRepository;
        this.env = env;
    }

    @PostConstruct
    public void createAdminAccount() {
        User currentAdminAccount = userRepository.findByRole(UserRole.ADMIN);
        if(currentAdminAccount != null) {
            return;
        }

        User adminAccount = new User();

        adminAccount.setEmail("admin@test.com");
        adminAccount.setName("Admin");

        adminAccount.setPassword(new BCryptPasswordEncoder()
                .encode(env.getProperty("admin.password")));

        adminAccount.setRole(UserRole.ADMIN);

        userRepository.save(adminAccount);
    }
}
