package com.taskmanager.usermanagement.controller;

import com.taskmanager.common.RequestContext;
import com.taskmanager.common.constants.CommonConstants;
import com.taskmanager.common.constants.JwtConstants;
import com.taskmanager.common.enums.Role;
import com.taskmanager.common.model.ServiceResponse;
import com.taskmanager.common.model.User;
import com.taskmanager.common.model.UserBase;
import com.taskmanager.common.model.request.RegistrationRequest;
import com.taskmanager.common.util.JwtUtils;
import com.taskmanager.usermanagement.mapper.UserBOMapper;
import com.taskmanager.usermanagement.model.request.StatusRoleUpdateRequest;
import com.taskmanager.usermanagement.model.request.UserUpdateRequest;
import com.taskmanager.usermanagement.model.response.UsersDetailResponse;
import com.taskmanager.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = CommonConstants.BASE_URL_USER_V1)
public class UserController {

    private final UserService userService;

    private final UserBOMapper userBOMapper;

    private final JwtUtils jwtUtils;

    @Autowired
    public UserController(UserService userService, UserBOMapper userBOMapper, JwtUtils jwtUtils) {
        this.userService = userService;
        this.userBOMapper = userBOMapper;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping(path = CommonConstants.API_USERS_REGISTER)
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "User Registration API", content = @Content(schema = @Schema(implementation = ServiceResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public ResponseEntity<UserBase> registration(@RequestBody RegistrationRequest registrationRequest) {
        User user = userBOMapper.mapFromRegistrationRequest(registrationRequest);
        user = userService.userRegistration(user);
        UserBase userBase = userBOMapper.mapFrom(user);
        return new ServiceResponse().build("User is Registered", HttpStatus.CREATED, userBase);
    }

    @GetMapping(path = CommonConstants.API_GET_USER_BY_USERNAME)
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User Details by username", content = @Content(schema = @Schema(implementation = ServiceResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public ResponseEntity<User> getUserDetailsByUsername(@PathVariable String username, @RequestHeader(name = RequestContext.HEADER_FIELD_AUTHORIZATION, defaultValue = JwtConstants.DEFAULT_AUTHORIZATION, required = true) String authorizationHeader) {
        jwtUtils.validateAccess(authorizationHeader, Role.SYSTEM);
        return new ServiceResponse().build("User Details", HttpStatus.OK, userService.getUserDetailsByUsername(username));
    }

    @GetMapping(path = CommonConstants.API_GET_USER_BY_ID)
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User Details by id", content = @Content(schema = @Schema(implementation = ServiceResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public ResponseEntity<User> getUserDetailsById(@PathVariable String id, @RequestHeader(name = RequestContext.HEADER_FIELD_AUTHORIZATION, defaultValue = JwtConstants.DEFAULT_AUTHORIZATION, required = true) String authorizationHeader) {
        jwtUtils.validateAccess(authorizationHeader, Role.getRoleList(Role.values()));
        return new ServiceResponse().build("User Details", HttpStatus.OK, userService.getUserDetailsById(id));
    }

    @GetMapping
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "All Users", content = @Content(schema = @Schema(implementation = UsersDetailResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public ResponseEntity<UsersDetailResponse> getAllUsers(@RequestHeader(name = RequestContext.HEADER_FIELD_AUTHORIZATION, defaultValue = JwtConstants.DEFAULT_AUTHORIZATION, required = true) String authorizationHeader, @QueryParam("includeInactive") Boolean includeInactive, @RequestParam(value = "isDetailsRequired", defaultValue = "true") Boolean isDetailsRequired) {
        jwtUtils.validateAccess(authorizationHeader, Role.getRoleList(Role.ADMIN, Role.MANAGER, Role.SYSTEM));
        List<User> users = userService.getAllUsers(includeInactive, isDetailsRequired);
        UsersDetailResponse response = new UsersDetailResponse(userBOMapper.mapToUserBase(users));
        return response.build("Loaded all the Users", HttpStatus.OK, response);
    }

    @PutMapping(path = CommonConstants.API_UPDATE_USER)
    @ApiResponses(value = {@ApiResponse(responseCode = "202", description = "Update User", content = @Content(schema = @Schema(implementation = ServiceResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public ResponseEntity<UserBase> updateUser(@PathVariable String id, @RequestBody UserUpdateRequest userUpdateRequest, @RequestHeader(name = RequestContext.HEADER_FIELD_AUTHORIZATION, defaultValue = JwtConstants.DEFAULT_AUTHORIZATION, required = true) String authorizationHeader) {
        jwtUtils.validateAccess(authorizationHeader, Role.getRoleList(Role.ADMIN, Role.MANAGER, Role.USER));
        User user = userBOMapper.mapFromUpdateRequest(userUpdateRequest);
        user.setId(id);
        user = userService.updateUser(user);
        UserBase userBase = userBOMapper.mapFrom(user);
        return new ServiceResponse().build("User is updated", HttpStatus.ACCEPTED, userBase);
    }

    @PatchMapping(path = CommonConstants.API_UPDATE_STATUS_ROLE)
    @ApiResponses(value = {@ApiResponse(responseCode = "202", description = "Update User's Role/Status", content = @Content(schema = @Schema(implementation = ServiceResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public ResponseEntity<Void> updateStatusRole(@PathVariable String id, @RequestHeader(name = RequestContext.HEADER_FIELD_AUTHORIZATION, defaultValue = JwtConstants.DEFAULT_AUTHORIZATION, required = true) String authorizationHeader, @RequestBody StatusRoleUpdateRequest statusRoleUpdateRequest) {
        jwtUtils.validateAccess(authorizationHeader, Role.getRoleList(Role.ADMIN));
        userService.updateStatusRole(id, statusRoleUpdateRequest);
        return new ServiceResponse().build("Update User Role/Status updated", HttpStatus.ACCEPTED, null);
    }

}
