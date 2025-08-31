package com.taskmanager.usermanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.common.RequestContext;
import com.taskmanager.common.constants.CommonConstants;
import com.taskmanager.common.enums.Role;
import com.taskmanager.common.model.ServiceResponse;
import com.taskmanager.common.model.User;
import com.taskmanager.common.model.UserBase;
import com.taskmanager.common.model.request.RegistrationRequest;
import com.taskmanager.common.util.JwtUtils;
import com.taskmanager.usermanagement.mapper.UserBOMapper;
import com.taskmanager.usermanagement.model.response.UsersDetailResponse;
import com.taskmanager.usermanagement.service.UserService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(value = CommonConstants.BASE_URL_USER)
public class UserController {

	private UserService userService;

	private UserBOMapper userBOMapper;

	private JwtUtils jwtUtils;

	@Autowired
	public UserController(UserService userService, UserBOMapper userBOMapper, JwtUtils jwtUtils) {
		this.userService = userService;
		this.userBOMapper = userBOMapper;
		this.jwtUtils = jwtUtils;
	}

	@PostMapping(path = CommonConstants.REGISTER)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "User Registration API", content = @Content(schema = @Schema(implementation = ServiceResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)) })
	public ResponseEntity<UserBase> registration(@RequestBody RegistrationRequest registrationRequest,
			@RequestHeader(name = RequestContext.HEADER_FIELD_AUTHORIZATION, required = true) String authorizationHeader) {
		jwtUtils.validateAccess(authorizationHeader, Role.getRoleList(Role.SYSTEM));
		User user = userBOMapper.mapFromRegistrationRequest(registrationRequest);
		user = userService.userRegistration(user);
		UserBase userBase = userBOMapper.mapFrom(user);
		return new ServiceResponse().build("User is Registered", HttpStatus.CREATED, userBase);
	}

	@GetMapping(path = CommonConstants.USERS_API_USER + "{identifier}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User Details", content = @Content(schema = @Schema(implementation = ServiceResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)) })
	public ResponseEntity<User> getUserDetailsByIdentifier(@PathVariable String identifier,
			@RequestHeader(name = RequestContext.HEADER_FIELD_AUTHORIZATION, required = true) String authorizationHeader) {
		jwtUtils.validateAccess(authorizationHeader, Role.getRoleList(Role.values()));
		return new ServiceResponse().build("User Details", HttpStatus.OK, userService.getUserDetails(identifier));
	}

	@GetMapping
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User Details", content = @Content(schema = @Schema(implementation = UsersDetailResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)) })
	public ResponseEntity<UsersDetailResponse> getAllUsers(
			@RequestHeader(name = RequestContext.HEADER_FIELD_AUTHORIZATION, required = true) String authorizationHeader) {
		jwtUtils.validateAccess(authorizationHeader, Role.getRoleList(Role.ADMIN, Role.MANAGER));
		List<User> users = userService.getAllUsers();
		UsersDetailResponse response = new UsersDetailResponse(userBOMapper.mapToUserBase(users));
		return response.build("Loaded all the Users", HttpStatus.OK, response);
	}
}
