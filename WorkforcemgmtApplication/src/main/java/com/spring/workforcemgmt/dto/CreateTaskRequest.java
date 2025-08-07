package com.spring.workforcemgmt.dto;

import java.time.LocalDate;

import com.spring.workforcemgmt.enums.Priority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {

    private String title;
    private LocalDate startDate;
    private LocalDate dueDate;
    private Long staffId;
    private Priority priority;
}
