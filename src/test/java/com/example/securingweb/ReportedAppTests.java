package com.example.securingweb;

import com.example.securingweb.controller.ProjectController;
import com.example.securingweb.controller.ProjectTaskController;
import com.example.securingweb.controller.UserController;
import com.example.securingweb.dao.ProjectRepository;
import com.example.securingweb.dao.ProjectTaskRepository;
import com.example.securingweb.model.Project;
import com.example.securingweb.model.ProjectTask;
import com.example.securingweb.service.ProjectService;
import com.example.securingweb.service.ProjectTaskService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.FormLoginRequestBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.Assert;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportedAppTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProjectRepository projectRepository;
	@MockBean
	private ProjectTaskRepository projectTaskRepository;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ProjectTaskService projectTaskService;


	@Test
	public void loginWithValidUserThenAuthenticated() throws Exception {
		FormLoginRequestBuilder login = formLogin()
			.user("user")
			.password("password");

		mockMvc.perform(login)
			.andExpect(authenticated().withUsername("user"));
	}

	@Test
	public void loginWithInvalidUserThenUnauthenticated() throws Exception {
		FormLoginRequestBuilder login = formLogin()
			.user("invalid")
			.password("invalidpassword");

		mockMvc.perform(login)
			.andExpect(unauthenticated());
	}

	@Test
	public void accessUnsecuredResourceThenOk() throws Exception {
		mockMvc.perform(get("/"))
			.andExpect(status().isOk());
	}

	@Test
	public void accessSecuredResourceUnauthenticatedThenRedirectsToLogin() throws Exception {
		mockMvc.perform(get("/invoice"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/loginuser"));
	}

	@Test
	@WithMockUser
	public void accessSecuredResourceAuthenticatedThenOk() throws Exception {
		mockMvc.perform(get("/hello"))
				.andExpect(status().isOk());
	}



	@Test
	public void getProjectTest() throws Exception {
		when(projectRepository.findAll()).thenReturn(Stream.of(getProject())
				.collect(Collectors.toList()));
		System.out.println(projectService.findAllProjects().size());
		Assert.assertEquals(1, projectService.findAllProjects().size());
	}

	@Test
	public void getTaskTest() throws Exception {
		when(projectTaskRepository.findAll()).thenReturn(Stream.of(getTask())
				.collect(Collectors.toList()));
		System.out.println(projectTaskService.findAllTasks().size());
		Assert.assertEquals(1, projectTaskService.findAllTasks().size());
	}

	@Test
	public void getProjectTaskTest() throws Exception {
		when(projectTaskRepository.findProjectTasksByProjectId(getProject().getId())).thenReturn(Stream.of(getTask())
				.collect(Collectors.toList()));

		Assert.assertEquals(1, projectTaskRepository.findProjectTasksByProjectId(getProject().getId()).size());
	}

	@Test
	public void getProjectTask2Test() throws Exception {
		when(projectTaskRepository.findProjectTasksByProjectId(getProject2().getId())).thenReturn(Stream.of(getTask())
				.collect(Collectors.toList()));

		Assert.assertEquals(0, projectTaskRepository.findProjectTasksByProjectId(getProject().getId()).size());
	}


	private Project getProject(){
		Project project = new Project();
		project.setId("KJHBKJHB79IGZ");
		project.setEstimatedHours(25.0);
		project.setProjectEnd(LocalDateTime.now());
		project.setHourRate(243.0);
		project.setName("Test");
		project.setDescription("Desc");
		project.setOwnerId("HJKBKH87G9IZUB");
		project.setProjectStart(LocalDateTime.of(2021, 1, 1, 12, 34));
		return project;
	}

	private Project getProject2(){
		Project project = new Project();
		project.setId("KJBOIB7GIUZGIUZ");
		project.setEstimatedHours(25.0);
		project.setProjectEnd(LocalDateTime.now());
		project.setHourRate(243.0);
		project.setName("Test");
		project.setDescription("Desc");
		project.setOwnerId("HJKBKH87G9IZUB");
		project.setProjectStart(LocalDateTime.of(2021, 1, 1, 12, 34));
		return project;
	}

	private ProjectTask getTask(){
		ProjectTask task = new ProjectTask();
		task.setProjectId("KJHBKJHB79IGZ");
		task.setTaskName("name");
		task.setDescription("desc");
		task.setId("JBIUH8");
		task.setStartDate(LocalDateTime.of(2021, 1, 1, 12, 34));
		task.setEndDate(LocalDateTime.of(2021, 1, 2, 12, 34));
		task.setHourRate(244.0);
		return task;
	}
}
