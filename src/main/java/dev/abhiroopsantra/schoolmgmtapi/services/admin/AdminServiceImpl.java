package dev.abhiroopsantra.schoolmgmtapi.services.admin;

import dev.abhiroopsantra.schoolmgmtapi.dto.UserDto;
import dev.abhiroopsantra.schoolmgmtapi.entities.User;
import dev.abhiroopsantra.schoolmgmtapi.enums.UserRole;
import dev.abhiroopsantra.schoolmgmtapi.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override public Page<UserDto> getStudents(Pageable pageable) {
        Page<User> students = userRepository.findAllByRole(UserRole.STUDENT, pageable);
        return students.map(student -> modelMapper.map(student, UserDto.class));
    }

    @Override public UserDto updateStudent(Long id, UserDto studentDto) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            return null;
        }

        User student = user.get();

        // we only allow updating the following fields of the student
        student.setName(studentDto.getName());
        student.setFatherName(studentDto.getFatherName());
        student.setMotherName(studentDto.getMotherName());
        student.setAddress(studentDto.getAddress());
        student.setStudentClass(studentDto.getStudentClass());

        student.setUpdatedAt(new Date());
        student.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        User updatedUser = userRepository.save(student);
        return modelMapper.map(updatedUser, UserDto.class);
    }
}
