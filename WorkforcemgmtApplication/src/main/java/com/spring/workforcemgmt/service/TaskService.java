package com.spring.workforcemgmt.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.spring.workforcemgmt.dto.CreateTaskRequest;
import com.spring.workforcemgmt.dto.TaskDto;
import com.spring.workforcemgmt.enums.Priority;
import com.spring.workforcemgmt.enums.Status;
import com.spring.workforcemgmt.model.Staff;
import com.spring.workforcemgmt.model.Task;

@Service
public class TaskService {
    private final Map<Long, Task> taskRepo = new HashMap<>();
    private final Map<Long, Staff> staffRepo = new HashMap<>();
    private final AtomicLong idGen = new AtomicLong();

    public TaskDto createTask(CreateTaskRequest req) {
        Staff staff = staffRepo.getOrDefault(req.getStaffId(), new Staff(req.getStaffId(), "Unknown"));
        staffRepo.putIfAbsent(req.getStaffId(), staff);

        Task task = Task.builder()
                .id(idGen.incrementAndGet())
                .title(req.getTitle())
                .startDate(req.getStartDate())
                .dueDate(req.getDueDate())
                .status(Status.ACTIVE)
                .priority(req.getPriority())
                .assignedTo(staff)
                .comments(new ArrayList<>())
                .activityHistory(new ArrayList<>())
                .build();

        task.getActivityHistory().add("Task created and assigned to " + staff.getName());
        taskRepo.put(task.getId(), task);
        return toDto(task);
    }

    public List<TaskDto> getAllTasks() {
        return taskRepo.values().stream()
                .filter(task -> task.getStatus() != Status.CANCELLED)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private TaskDto toDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .startDate(task.getStartDate())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
                .priority(task.getPriority())
                .assignedTo(task.getAssignedTo().getName())
                .comments(task.getComments())
                .activityHistory(task.getActivityHistory())
                .build();
    }
    public TaskDto reassignTask(Long taskId, Long newStaffId) {
        Task task = taskRepo.get(taskId);
        if (task == null) throw new RuntimeException("Task not found");

        task.setStatus(Status.CANCELLED);
        task.getActivityHistory().add("Task reassigned: marked as CANCELLED");

        Staff newStaff = staffRepo.getOrDefault(newStaffId, new Staff(newStaffId, "Unknown"));
        staffRepo.putIfAbsent(newStaffId, newStaff);

        Task newTask = Task.builder()
                .id(idGen.incrementAndGet())
                .title(task.getTitle())
                .startDate(task.getStartDate())
                .dueDate(task.getDueDate())
                .status(Status.ACTIVE)
                .priority(task.getPriority())
                .assignedTo(newStaff)
                .comments(new ArrayList<>(task.getComments()))
                .activityHistory(new ArrayList<>(task.getActivityHistory()))
                .build();

        newTask.getActivityHistory().add("Task reassigned to " + newStaff.getName());
        taskRepo.put(newTask.getId(), newTask);
        return toDto(newTask);
    }
    public List<TaskDto> getTasksByDateRange(LocalDate start, LocalDate end) {
        return taskRepo.values().stream()
                .filter(task -> task.getStatus() != Status.CANCELLED)
                .filter(task -> {
                    LocalDate s = task.getStartDate();
                    return (s != null && !s.isBefore(start) && !s.isAfter(end));
                })
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    public List<TaskDto> getSmartTasksByDateRange(LocalDate start, LocalDate end) {
        return taskRepo.values().stream()
                .filter(task -> task.getStatus() == Status.ACTIVE)
                .filter(task -> {
                    LocalDate s = task.getStartDate();
                    return (s != null && (
                            (!s.isBefore(start) && !s.isAfter(end)) || // within range
                            s.isBefore(start)                          // before range, still active
                    ));
                })
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    public TaskDto updateTaskPriority(Long taskId, Priority newPriority) {
        Task task = taskRepo.get(taskId);
        if (task == null) throw new RuntimeException("Task not found");

        task.setPriority(newPriority);
        task.getActivityHistory().add("Priority updated to " + newPriority);
        return toDto(task);
    }

    public List<TaskDto> getTasksByPriority(Priority priority) {
        return taskRepo.values().stream()
                .filter(task -> task.getStatus() != Status.CANCELLED)
                .filter(task -> task.getPriority() == priority)
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    public TaskDto addComment(Long taskId, String comment) {
        Task task = taskRepo.get(taskId);
        if (task == null) throw new RuntimeException("Task not found");

        String log = "Comment added: " + comment;
        task.getComments().add(comment);
        task.getActivityHistory().add(log);
        return toDto(task);
    }



}

