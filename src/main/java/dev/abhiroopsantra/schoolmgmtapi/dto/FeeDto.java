package dev.abhiroopsantra.schoolmgmtapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter public class FeeDto {
    private Long   id;
    private String month;
    private String givenBy;
    private Double amount;
    private String description;
    private Date   createdAt;
    private Long   studentId;
}
