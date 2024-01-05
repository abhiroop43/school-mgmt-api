package dev.abhiroopsantra.schoolmgmtapi.services.admin;

import dev.abhiroopsantra.schoolmgmtapi.dto.FeeDto;
import dev.abhiroopsantra.schoolmgmtapi.dto.StudentLeaveDto;
import dev.abhiroopsantra.schoolmgmtapi.dto.UserDto;
import dev.abhiroopsantra.schoolmgmtapi.enums.StudentLeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    UserDto postStudent(UserDto studentDto);

    Page<UserDto> getStudents(Pageable pageable);

    UserDto updateStudent(Long id, UserDto studentDto);

    Boolean deleteStudent(Long id);

    FeeDto payFee(Long studentId, FeeDto feeDto);

    UserDto getStudentById(Long id);

    boolean updateStudentLeaveStatus(Long leaveId, StudentLeaveStatus leaveStatus);

    Page<StudentLeaveDto> getAllAppliedLeaves(Pageable pageable);
}
