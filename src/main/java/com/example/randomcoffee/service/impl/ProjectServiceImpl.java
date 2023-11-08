package com.example.randomcoffee.service.impl;

import com.example.randomcoffee.exceptions.CustomException;
import com.example.randomcoffee.model.db.entity.Country;
import com.example.randomcoffee.model.db.entity.Hobby;
import com.example.randomcoffee.model.db.entity.Project;
import com.example.randomcoffee.model.db.repository.ProjectRepo;
import com.example.randomcoffee.model.enums.EntityStatus;
import com.example.randomcoffee.rest_api.dto.request.ProjectRequest;
import com.example.randomcoffee.rest_api.dto.response.CountryResponse;
import com.example.randomcoffee.rest_api.dto.response.HobbyResponse;
import com.example.randomcoffee.rest_api.dto.response.ProjectResponse;
import com.example.randomcoffee.service.ProjectService;
import com.example.randomcoffee.utils.PaginationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepo projectRepo;
    private final ObjectMapper objectMapper;

    @Override
    public ProjectResponse getProjectDto(Long id) {
        String errorMsg = String.format("Project with id %d not found", id);
        Project project = projectRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        ProjectResponse result = objectMapper.convertValue(project, ProjectResponse.class);
        return result;
    }

    @Override
    public Page<ProjectResponse> getAllProjects(Integer page, Integer perPage, String sort, Sort.Direction order) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
        Page<Project> projectPage = projectRepo.findAll(pageRequest);
        List<ProjectResponse> projectsList = projectPage.getContent().stream()
                .map(p -> objectMapper.convertValue(p, ProjectResponse.class))
                .collect(Collectors.toList());
        return new PageImpl<>(projectsList);
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request) {
        String projectCode = request.getProjectCode();
        projectRepo.findByProjectCode(projectCode)
                .ifPresent(c -> {
                    throw new CustomException("This Project already exists", HttpStatus.BAD_REQUEST);
                });
        if (!StringUtils.isBlank(request.getProjectCode())) {
            Project project = objectMapper.convertValue(request, Project.class);
            project.setCreatedAt(LocalDateTime.now());
            project.setStatus(EntityStatus.CREATED);
            Project save = projectRepo.save(project);
            ProjectResponse result = objectMapper.convertValue(save, ProjectResponse.class);
            return result;
        }
        throw new CustomException("Project code can not be empty", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        String errorMsg = String.format("Project with id %d not found", id);
        Project project = projectRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));

        project.setTitle(StringUtils.isBlank(request.getTitle()) ? project.getTitle() : request.getTitle());
        project.setDescription(StringUtils.isBlank(request.getDescription()) ? project.getDescription() : request.getDescription());
        project.setProjectCode(StringUtils.isBlank(request.getProjectCode()) ? project.getProjectCode() : request.getProjectCode());
        project.setUpdatedAt(LocalDateTime.now());
        project.setStatus(EntityStatus.UPDATED);
        Project save = projectRepo.save(project);
        ProjectResponse result = objectMapper.convertValue(save, ProjectResponse.class);
        return result;
    }

    @Override
    public void deleteProject(Long id) {
        String errorMsg = String.format("Project with id %d not found", id);
        Project project = projectRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        if (!project.getStatus().equals(EntityStatus.DELETED)) {
            project.setStatus(EntityStatus.DELETED);
            project.setUpdatedAt(LocalDateTime.now());
            projectRepo.save(project);
            return;
        }
        throw new CustomException("This hobby was already deleted", HttpStatus.NOT_FOUND);
    }

    @Override
    public Page<ProjectResponse> getProjectByTitle(Integer page, Integer perPage, String sort, Sort.Direction order, String title) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
        Page<Project> projectPage = projectRepo.findByTitle(pageRequest, title);
        List<ProjectResponse> projectsList = projectPage.getContent().stream()
                .map(p -> objectMapper.convertValue(p, ProjectResponse.class))
                .collect(Collectors.toList());
        return new PageImpl<>(projectsList);
    }
}
