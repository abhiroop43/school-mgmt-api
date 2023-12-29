package dev.abhiroopsantra.schoolmgmtapi.dto;

import dev.abhiroopsantra.schoolmgmtapi.enums.Gender;
import dev.abhiroopsantra.schoolmgmtapi.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter @Getter public class UserDto {

    private Long     id;
    private String   name;
    private String   email;
    private UserRole role;
    private String   fatherName;
    private String   motherName;
    private String   StudentClass;
    private Date     dateOfBirth;
    private String   address;
    private Gender   gender;
    private Date     CreatedAt;
    private String   CreatedBy;
    private Date     UpdatedAt;
}
