package dev.abhiroopsantra.schoolmgmtapi.entities;

import dev.abhiroopsantra.schoolmgmtapi.enums.Gender;
import dev.abhiroopsantra.schoolmgmtapi.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter @Getter @Entity @Table(name = "users") public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long     id;
    @Column(nullable = false)
    private String   name;
    @Column(nullable = false, unique = true)
    private String   email;
    @Column(nullable = false)
    private String   password;
    private String   fatherName;
    private String   motherName;
    private String   StudentClass;
    private Date     dateOfBirth;
    private String   address;
    private Gender   gender    = Gender.MALE;
    private UserRole role      = UserRole.STUDENT;
    private Date     CreatedAt = new Date();
    private String   CreatedBy = "SYSTEM";
    private Date     UpdatedAt;
    private String   UpdatedBy;
}
