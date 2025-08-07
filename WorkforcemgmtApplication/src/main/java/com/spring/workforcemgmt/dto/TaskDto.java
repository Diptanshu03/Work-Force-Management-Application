package com.spring.workforcemgmt.dto;

import java.time.LocalDate;
import java.util.List;

import com.spring.workforcemgmt.enums.Priority;
import com.spring.workforcemgmt.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate dueDate;
    private Status status;
    private Priority priority;
    private String assignedTo;
    private List<String> comments;
    private List<String> activityHistory;
}
