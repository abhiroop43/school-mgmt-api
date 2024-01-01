package dev.abhiroopsantra.schoolmgmtapi.services.student;

import dev.abhiroopsantra.schoolmgmtapi.dto.UserDto;
import dev.abhiroopsantra.schoolmgmtapi.entities.User;
import dev.abhiroopsantra.schoolmgmtapi.exceptions.BadRequestException;
import dev.abhiroopsantra.schoolmgmtapi.exceptions.NotFoundException;
import dev.abhiroopsantra.schoolmgmtapi.exceptions.UnauthorizedException;
import dev.abhiroopsantra.schoolmgmtapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service @RequiredArgsConstructor public class StudentServiceImpl implements StudentService {
    private final UserRepository userRepository;
    private final ModelMapper    modelMapper;

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
}
