package dev.abhiroopsantra.schoolmgmtapi.services.student;

import dev.abhiroopsantra.schoolmgmtapi.dto.StudentLeaveDto;
import dev.abhiroopsantra.schoolmgmtapi.dto.UserDto;
import dev.abhiroopsantra.schoolmgmtapi.exceptions.NotFoundException;

public interface StudentService {
    UserDto getStudentById(Long id);

    StudentLeaveDto applyForLeave(StudentLeaveDto studentLeaveDto);
}
