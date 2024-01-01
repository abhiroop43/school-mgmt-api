package dev.abhiroopsantra.schoolmgmtapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.abhiroopsantra.schoolmgmtapi.enums.StudentLeaveStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Setter @Getter @Entity @Table(name = "student_leaves") public class StudentLeave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long               id;
    private String             subject;
    private String             body;
    private Date               leaveStartDate;
    private Date               leaveEndDate;
    private StudentLeaveStatus studentLeaveStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User student;
}
