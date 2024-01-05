package dev.abhiroopsantra.schoolmgmtapi.services.student;

import dev.abhiroopsantra.schoolmgmtapi.dto.StudentLeaveDto;
import dev.abhiroopsantra.schoolmgmtapi.dto.UserDto;
import dev.abhiroopsantra.schoolmgmtapi.entities.StudentLeave;
import dev.abhiroopsantra.schoolmgmtapi.entities.User;
import dev.abhiroopsantra.schoolmgmtapi.enums.StudentLeaveStatus;
import dev.abhiroopsantra.schoolmgmtapi.exceptions.BadRequestException;
import dev.abhiroopsantra.schoolmgmtapi.repositories.StudentLeaveRepository;
import dev.abhiroopsantra.schoolmgmtapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service @RequiredArgsConstructor public class StudentServiceImpl implements StudentService {
    private final UserRepository         userRepository;
    private final ModelMapper            modelMapper;
    private final StudentLeaveRepository studentLeaveRepository;

    @Override public UserDto getStudentById(Long id) {
        // get the student with the id
        Optional<User> student = userRepository.findById(id);

        // check if the student exists
        if (student.isEmpty()) {
            throw new BadRequestException(
                    "Either the student does not exist of you are not authorized to view this student's details");
        }

        // get the currently logged in user
        String currentLoggedInUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // check if the currently logged in user is the same as the student with the id
        if (!currentLoggedInUserEmail.equals(student.get().getEmail())) {
            throw new BadRequestException(
                    "Either the student does not exist of you are not authorized to view this student's details");
        }

        return modelMapper.map(student.get(), UserDto.class);
    }

    @Override public StudentLeaveDto applyForLeave(StudentLeaveDto studentLeaveDto) {
        // get the currently logged in user
        String currentLoggedInUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // get the student with the email
        Optional<User> student = userRepository.findFirstByEmail(currentLoggedInUserEmail);

        // check if the student exists
        if (student.isEmpty()) {
            throw new BadRequestException(
                    "Either the student does not exist of you are not authorized to apply for leave");
        }

        // TODO: Validate the leave dates

        // add new leave to the student
        StudentLeave studentLeave = modelMapper.map(studentLeaveDto, StudentLeave.class);
        studentLeave.setStudentLeaveStatus(StudentLeaveStatus.PENDING);
        studentLeave.setStudent(student.get());
        studentLeave.setCreatedAt(new java.util.Date());
        studentLeave.setCreatedBy(currentLoggedInUserEmail);

        StudentLeave submittedLeave = studentLeaveRepository.save(studentLeave);

        return modelMapper.map(submittedLeave, StudentLeaveDto.class);
    }

    @Override public List<StudentLeaveDto> getAllAppliedLeavesByStudentId(Long id) {
        // get the student with the id
        Optional<User> student = userRepository.findById(id);

        // check if the student exists
        if (student.isEmpty()) {
            throw new BadRequestException(
                    "Either the student does not exist of you are not authorized to view this student's details");
        }

        // get the currently logged in user
        String currentLoggedInUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // check if the currently logged in user is the same as the student with the id
        if (!currentLoggedInUserEmail.equals(student.get().getEmail())) {
            throw new BadRequestException(
                    "Either the student does not exist of you are not authorized to view this student's details");
        }

        // get all the leaves applied by the student
        List<StudentLeave> studentLeaves = studentLeaveRepository.findAllByStudentId(id);

        return studentLeaves.stream().map(studentLeave -> modelMapper.map(studentLeave, StudentLeaveDto.class))
                            .toList();
    }
}
