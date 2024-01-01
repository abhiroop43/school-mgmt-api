package dev.abhiroopsantra.schoolmgmtapi.services.admin;

import dev.abhiroopsantra.schoolmgmtapi.dto.FeeDto;
import dev.abhiroopsantra.schoolmgmtapi.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    UserDto postStudent(UserDto studentDto);

    Page<UserDto> getStudents(Pageable pageable);

    UserDto updateStudent(Long id, UserDto studentDto);

    Boolean deleteStudent(Long id);

    FeeDto payFee(Long studentId, FeeDto feeDto);

    UserDto getStudentById(Long id);
}
