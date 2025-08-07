package com.spring.workforcemgmt.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.workforcemgmt.dto.CreateTaskRequest;
import com.spring.workforcemgmt.dto.TaskDto;
import com.spring.workforcemgmt.enums.Priority;
import com.spring.workforcemgmt.service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

	private final TaskService taskService;

	@PostMapping
	public TaskDto create(@RequestBody CreateTaskRequest request) {
		return taskService.createTask(request);
	}

	@GetMapping
	public List<TaskDto> getAll() {
		return taskService.getAllTasks();
	}

	@PutMapping("/{id}/reassign/{staffId}")
	public TaskDto reassign(@PathVariable Long id, @PathVariable Long staffId) {
		return taskService.reassignTask(id, staffId);
	}

	@GetMapping("/range")
	public List<TaskDto> getTasksInDateRange(@RequestParam("start") String startDate,
			@RequestParam("end") String endDate) {
		LocalDate start = LocalDate.parse(startDate);
		LocalDate end = LocalDate.parse(endDate);
		return taskService.getTasksByDateRange(start, end);
	}
	@GetMapping("/smart-range")
	public List<TaskDto> getSmartTasksInDateRange(
	        @RequestParam("start") String startDate,
	        @RequestParam("end") String endDate
	) {
	    LocalDate start = LocalDate.parse(startDate);
	    LocalDate end = LocalDate.parse(endDate);
	    return taskService.getSmartTasksByDateRange(start, end);
	}
	@PutMapping("/{id}/priority")
	public TaskDto updatePriority(
	        @PathVariable Long id,
	        @RequestParam("level") Priority priority
	) {
	    return taskService.updateTaskPriority(id, priority);
	}

	@GetMapping("/priority/{level}")
	public List<TaskDto> getByPriority(@PathVariable Priority level) {
	    return taskService.getTasksByPriority(level);
	}
	@PostMapping("/{id}/comment")
	public TaskDto addComment(
	        @PathVariable Long id,
	        @RequestParam("text") String commentText
	) {
	    return taskService.addComment(id, commentText);
	}

}
