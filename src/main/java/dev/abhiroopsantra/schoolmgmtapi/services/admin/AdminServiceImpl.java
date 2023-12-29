package dev.abhiroopsantra.schoolmgmtapi.services.admin;

import dev.abhiroopsantra.schoolmgmtapi.dto.UserDto;
import dev.abhiroopsantra.schoolmgmtapi.entities.User;
import dev.abhiroopsantra.schoolmgmtapi.enums.UserRole;
import dev.abhiroopsantra.schoolmgmtapi.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service public class AdminServiceImpl implements AdminService {

    private final Environment    env;
    private final UserRepository userRepository;
    private final ModelMapper    modelMapper;

    public AdminServiceImpl(UserRepository userRepository, Environment env, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.env            = env;
        this.modelMapper    = modelMapper;
    }

    @PostConstruct public void createAdminAccount() {
        User currentAdminAccount = userRepository.findByRole(UserRole.ADMIN);
        if (currentAdminAccount != null) {
            return;
        }

        User adminAccount = new User();

        adminAccount.setEmail("admin@test.com");
        adminAccount.setName("Admin");

        adminAccount.setPassword(new BCryptPasswordEncoder().encode(env.getProperty("admin.password")));

        adminAccount.setRole(UserRole.ADMIN);

        userRepository.save(adminAccount);
    }

    @Override public UserDto postStudent(UserDto studentDto) {
        Optional<User> user = userRepository.findFirstByEmail(studentDto.getEmail());

        if (user.isPresent()) {
            return null;
        }

        User student = modelMapper.map(studentDto, User.class);
        student.setPassword(new BCryptPasswordEncoder().encode(env.getProperty("default.password")));
        student.setRole(UserRole.STUDENT);
        student.setCreatedAt(new Date());

        User createdUser = userRepository.save(student);
        return modelMapper.map(createdUser, UserDto.class);
    }
}
