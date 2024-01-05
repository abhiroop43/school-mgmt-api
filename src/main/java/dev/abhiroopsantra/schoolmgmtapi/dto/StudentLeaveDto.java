package dev.abhiroopsantra.schoolmgmtapi.dto;

import dev.abhiroopsantra.schoolmgmtapi.enums.StudentLeaveStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter public class StudentLeaveDto {
    private Long               id;
    private String             subject;
    private String             body;
    private Date               leaveStartDate;
    private Date               leaveEndDate;
    private StudentLeaveStatus studentLeaveStatus;
    private Date               createdAt;
    private String             createdBy;
    private Date               updatedAt;
    private String             updatedBy;
    private Long               studentId;
}
