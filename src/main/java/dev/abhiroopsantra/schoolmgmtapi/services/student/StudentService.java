package dev.abhiroopsantra.schoolmgmtapi.services.student;

import dev.abhiroopsantra.schoolmgmtapi.dto.UserDto;

public interface StudentService {
    UserDto getStudentById(Long id);
}
