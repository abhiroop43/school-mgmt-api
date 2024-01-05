package dev.abhiroopsantra.schoolmgmtapi.services.admin;

import dev.abhiroopsantra.schoolmgmtapi.dto.FeeDto;
import dev.abhiroopsantra.schoolmgmtapi.dto.UserDto;
import dev.abhiroopsantra.schoolmgmtapi.entities.Fee;
import dev.abhiroopsantra.schoolmgmtapi.entities.StudentLeave;
import dev.abhiroopsantra.schoolmgmtapi.entities.User;
import dev.abhiroopsantra.schoolmgmtapi.enums.StudentLeaveStatus;
import dev.abhiroopsantra.schoolmgmtapi.enums.UserRole;
import dev.abhiroopsantra.schoolmgmtapi.repositories.FeeRepository;
import dev.abhiroopsantra.schoolmgmtapi.repositories.StudentLeaveRepository;
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

    private final Environment            env;
    private final UserRepository         userRepository;
    private final ModelMapper            modelMapper;
    private final FeeRepository          feeRepository;
    private final StudentLeaveRepository studentLeaveRepository;

    public AdminServiceImpl(
            UserRepository userRepository, Environment env, ModelMapper modelMapper, FeeRepository feeRepository,
            StudentLeaveRepository studentLeaveRepository
                           ) {
        this.userRepository         = userRepository;
        this.env                    = env;
        this.modelMapper            = modelMapper;
        this.feeRepository          = feeRepository;
        this.studentLeaveRepository = studentLeaveRepository;
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
        adminAccount.setCreatedAt(new Date());
        adminAccount.setCreatedBy("System");
        adminAccount.setIsActive(true);

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
        student.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        student.setIsActive(true);

        User createdUser = userRepository.save(student);
        return modelMapper.map(createdUser, UserDto.class);
    }

    @Override public Page<UserDto> getStudents(Pageable pageable) {
        // list only active students
        Page<User> students = userRepository.findByRoleAndIsActive(UserRole.STUDENT, true, pageable);

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
        student.setIsActive(true);

        student.setUpdatedAt(new Date());
        student.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        User updatedUser = userRepository.save(student);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override public Boolean deleteStudent(Long id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            return false;
        }

        // soft delete the student
        User student = user.get();
        student.setIsActive(false);
        student.setUpdatedAt(new Date());
        student.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        userRepository.save(student);

        return true;
    }

    @Override public FeeDto payFee(Long studentId, FeeDto feeDto) {
        Optional<User> user = userRepository.findById(studentId);

        if (user.isEmpty()) {
            return null;
        }

        User student = user.get();

        Fee fee = modelMapper.map(feeDto, Fee.class);
        fee.setStudent(student);
        fee.setCreatedAt(new Date());

        Fee paidFees = feeRepository.save(fee);
        return modelMapper.map(paidFees, FeeDto.class);
    }

    @Override public UserDto getStudentById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> modelMapper.map(value, UserDto.class)).orElse(null);
    }

    @Override public boolean updateStudentLeaveStatus(Long leaveId, StudentLeaveStatus leaveStatus) {
        // get the leave
        Optional<StudentLeave> studentLeave = studentLeaveRepository.findById(leaveId);

        // check if the leave exists
        if (studentLeave.isEmpty()) {
            return false;
        }

        // update the leave status
        StudentLeave leave = studentLeave.get();
        leave.setStudentLeaveStatus(leaveStatus);
        leave.setUpdatedAt(new Date());
        leave.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        studentLeaveRepository.save(leave);

        return true;
    }
}
