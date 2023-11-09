package com.example.randomcoffee.rest_api.rest_controllers;

import com.example.randomcoffee.rest_api.dto.request.HobbyRequest;
import com.example.randomcoffee.rest_api.dto.request.ProjectRequest;
import com.example.randomcoffee.rest_api.dto.response.HobbyResponse;
import com.example.randomcoffee.rest_api.dto.response.ProjectResponse;
import com.example.randomcoffee.service.impl.ProjectServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Проекты")
@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectRestController {

    private final ProjectServiceImpl projectService;
    @PostMapping("/")
    @Operation(summary = "Заведение нового проекта")
    public ProjectResponse createProject(@RequestBody ProjectRequest request) {

        return projectService.createProject(request);
    }

    @Operation(summary = "Найти проект по id")
    @GetMapping("/{id}")
    public ProjectResponse getProject(@PathVariable Long id) {

        return projectService.getProjectDto(id);
    }

    @Operation(summary = "Удалить проект по id")
    @DeleteMapping("/{id}")
    public void DeleteProject(@PathVariable Long id) {

        projectService.deleteProject(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Внести изменения в существующий проект")
    public ProjectResponse updateProject(@PathVariable Long id, @RequestBody ProjectRequest request) {

        return projectService.updateProject(id, request);
    }

    @Operation(summary = "Список всех проектов")
    @GetMapping("/all")
    public Page<ProjectResponse> getAllProjects(@RequestParam(defaultValue = "1") Integer page,
                                             @RequestParam(defaultValue = "10") Integer perPage,
                                             @RequestParam(defaultValue = "title") String sort,
                                             @RequestParam(defaultValue = "ASC") Sort.Direction order) {
        return projectService.getAllProjects(page, perPage, sort, order);
    }

    @Operation(summary = "Список всех проектов по указанному названию")
    @GetMapping("/by-title")
    public Page<ProjectResponse> getProjectByTitle(@RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer perPage,
                                          @RequestParam(defaultValue = "title") String sort,
                                          @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                          @RequestParam(required = false) String title) {
        return projectService.getProjectByTitle(page, perPage, sort, order, title);
    }
}
