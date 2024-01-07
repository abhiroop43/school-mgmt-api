package dev.abhiroopsantra.schoolmgmtapi.dto;

import dev.abhiroopsantra.schoolmgmtapi.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter public class TeacherDto {
    private Long   id;
    private String name;
    private Gender gender;
    private String department;
    private String qualification;
    private Date   dateOfBirth;
    private String address;

    private Date    createdAt;
    private String  createdBy;
    private Date    updatedAt;
    private String  updatedBy;
    private Boolean isActive;
}
