package com.example.randomcoffee.service;

import com.example.randomcoffee.rest_api.dto.request.HobbyRequest;
import com.example.randomcoffee.rest_api.dto.request.ProjectRequest;
import com.example.randomcoffee.rest_api.dto.response.HobbyResponse;
import com.example.randomcoffee.rest_api.dto.response.ProjectResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface ProjectService {

    ProjectResponse getProjectDto(Long id);
    Page<ProjectResponse> getAllProjects(Integer page, Integer perPage, String sort, Sort.Direction order);

    ProjectResponse createProject(ProjectRequest request);
    ProjectResponse updateProject(Long id, ProjectRequest request);
    void deleteProject(Long id);
    Page<ProjectResponse> getProjectByTitle(Integer page, Integer perPage, String sort, Sort.Direction order, String title);
}
