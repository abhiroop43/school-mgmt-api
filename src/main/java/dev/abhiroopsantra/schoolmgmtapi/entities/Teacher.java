package dev.abhiroopsantra.schoolmgmtapi.entities;

import dev.abhiroopsantra.schoolmgmtapi.enums.Gender;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity @Getter @Setter public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long   id;
    private String name;
    private Gender gender;
    private String department;
    private String qualification;
    private Date   dateOfBirth;
    private String address;

    private Date    createdAt = new Date();
    private String  createdBy = "SYSTEM";
    private Date    updatedAt;
    private String  updatedBy;
    private Boolean isActive  = true;
}
