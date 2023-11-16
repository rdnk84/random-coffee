package com.example.randomcoffee.service.impl;

import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.model.db.entity.Country;
import com.example.randomcoffee.model.db.entity.Project;
import com.example.randomcoffee.model.db.repository.ProjectRepo;
import com.example.randomcoffee.model.enums.EntityStatus;
import com.example.randomcoffee.rest_api.dto.request.ProjectRequest;
import com.example.randomcoffee.rest_api.dto.response.ProjectResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.randomcoffee.utils.PaginationUtil.getPageRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProjectServiceImplTest {

    @InjectMocks
    ProjectServiceImpl projectService;

    @Mock
    ProjectRepo projectRepo;

    @Spy
    ObjectMapper mapper;


    @Test
    void getProjectDto() {
        Project project = new Project();
        project.setId(1L);
        when(projectRepo.findById(project.getId())).thenReturn(Optional.of(project));
        ProjectResponse result = projectService.getProjectDto(project.getId());
        assertEquals(result.getId(), project.getId());
    }

    @Test
    void getAllProjects() {
        Project project = new Project();
        project.setId(1L);

        Set<Project> projectSet = (Set.of(project));
        List<Project> projectList = projectSet.stream().toList();
        Pageable pageRequest = getPageRequest(1, 2, "title", Sort.Direction.ASC);
        Page<Project> projectPage = new PageImpl<Project>(projectList, pageRequest, 1);
        when(projectRepo.findAll(any(Pageable.class))).thenReturn(projectPage);
        Page<ProjectResponse> result = projectService.getAllProjects(1, 2, "title", Sort.Direction.ASC);
        assertEquals(result.getNumberOfElements(), 1);
    }

    @Test
    void createProject() {
        ProjectRequest request = new ProjectRequest();
        request.setTitle("Project1");
        request.setProjectCode("LFBE");
        request.setDescription("Optimized context-sensitive array");

        Project project = new Project();
        project.setId(1L);

        when(projectRepo.save(ArgumentMatchers.any(Project.class))).thenReturn(project);
        ProjectResponse result = projectService.createProject(request);
        assertEquals(result.getId(), project.getId());
        assertEquals(result.getProjectCode(), project.getProjectCode());
    }

    @Test
    void updateProject() {
        ProjectRequest request = new ProjectRequest();
        request.setTitle("another project");

        Project project = new Project();
        project.setId(1L);
        project.setProjectCode("JJJJ");
        when(projectRepo.findById(project.getId())).thenReturn(Optional.of(project));
        when(projectRepo.save(ArgumentMatchers.any(Project.class))).thenReturn(project);
        ProjectResponse result = projectService.updateProject(project.getId(), request);
        assertEquals(result.getTitle(), request.getTitle());
        assertEquals(result.getProjectCode(), project.getProjectCode());
    }

    @Test
    void deleteProject() {
        Project project = new Project();
        project.setId(1L);
        project.setStatus(EntityStatus.CREATED);
        when(projectRepo.findById(project.getId())).thenReturn(Optional.of(project));
        projectService.deleteProject(project.getId());
        verify(projectRepo, times(1)).save(ArgumentMatchers.any(Project.class));
    }

    @Test
    void getProjectByTitle() {
        Project project = new Project();
        project.setId(1L);
        project.setTitle("some project");
        List<Project> projects = (List.of(project));
        Pageable pageRequest = getPageRequest(1, 2, "title", Sort.Direction.ASC);
        Page<Project> projectPage = new PageImpl<Project>(projects, pageRequest, 1);
        when(projectRepo.findByTitle(any(Pageable.class), anyString())).thenReturn(projectPage);
        Page<ProjectResponse> result = projectService.getProjectByTitle(1, 2, "title", Sort.Direction.ASC, project.getTitle());
        assertEquals(result.getNumberOfElements(), 1);
    }
}