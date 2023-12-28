package dev.abhiroopsantra.schoolmgmtapi.dto.Auth;

import dev.abhiroopsantra.schoolmgmtapi.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private UserRole role;
}
