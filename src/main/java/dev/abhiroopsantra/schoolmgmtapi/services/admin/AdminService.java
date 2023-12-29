package dev.abhiroopsantra.schoolmgmtapi.services.admin;

import dev.abhiroopsantra.schoolmgmtapi.dto.UserDto;

public interface AdminService {
    UserDto postStudent(UserDto studentDto);
}
