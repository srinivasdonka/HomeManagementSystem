package com.homemanagement.controller;

import java.util.ArrayList;
import java.util.List;

import com.homemanagement.domain.PrivilegesMapping;
import com.homemanagement.domain.User;
import com.homemanagement.dto.PrivilegesMappingDTO;
import com.homemanagement.dto.RoleDTO;
import com.homemanagement.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.jboss.logging.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.homemanagement.domain.Roles;
import com.homemanagement.dto.ServiceStatus;
@RestController
@Api(value = "Authentication")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private ModelMapper modelMapper;
    private static final Logger logger = Logger.getLogger(AuthController.class);
    /**
     * This method is used to Add the privileges to the Role.
     *
     * @param privileges specify the privilege info
     * @return the service status class object with response status and payload         .
     */
    @ApiOperation(value = "Add a privileges")
    @PostMapping("/auth/addPrivileges")
    ServiceStatus<Object> createPrivileges(@RequestBody PrivilegesMapping privileges) {
        logger.info("createPrivileges");
        return authService.createPrivilegesToUser(privileges);
    }
    /**
     * This method is used to update the privileges to the Role.
     *
     * @param privilegesMappingDto specify the privilege info
     * @return the service status class object with response status and payload
     * .
     */
    @ApiOperation(value = "Update a privilege")
    @PutMapping("/auth/updatePrivilege")
    ServiceStatus<Object> updatePrivilege(@RequestBody PrivilegesMappingDTO privilegesMappingDto) {
        PrivilegesMapping privilegesMapping = this.modelMapper.map(privilegesMappingDto, PrivilegesMapping.class);
        return authService.updatePrivilegesToUser(privilegesMapping);
    }
    @ApiOperation(value = "View a list of available products", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping("/auth/getPrivileges")
    ServiceStatus<Object> getPrivileges(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        return authService.getPrivilegesToUser(page, size);
    }
    @ApiOperation(value = "Search a product with an ID", response = User.class)
    @GetMapping("/auth/getPrivilegesByRole")
    ServiceStatus<Object> getPrivilegesByRole(@RequestParam("roleId") String roleId,
											  @RequestParam("userId") String userId) {
        return authService.getPrivilegesByUser(userId);
    }
    /**
     * This method is use to Creates the Role.
     *
     * @param roleDto specify the Role Info
     * @return the service status class object with response status and payload.
     */
    @ApiOperation(value = "Add a role")
    @PostMapping("/auth/createRole")
    ServiceStatus<Object> createRole(@RequestBody RoleDTO roleDto) {
        Roles role = this.modelMapper.map(roleDto, Roles.class);
        return authService.createRoleForUser(role);
    }
    /**
     * This method is use to update the role.
     *
     * @param roleDto specify the Role Info
     * @return the service status class object with response status and payload.
     */
    @ApiOperation(value = "Update a product")
    @PutMapping("/auth/updateRole")
    ServiceStatus<Object> updateRole(@RequestBody RoleDTO roleDto) {
        Roles role = this.modelMapper.map(roleDto, Roles.class);
        return authService.updateRole(role);
    }
    /**
     * This method is used to get all the Roles.
     *
     * @return the service status class object with response status and payload
     */
    @ApiOperation(value = "Search a product with an ID", response = User.class)
    @GetMapping("/auth/getRoles")
    ServiceStatus<Object> getRoles(@RequestParam("page") Integer page,
                                   @RequestParam("size") Integer size) {
        return authService.getRoleForUser(page, size);
    }
    /**
     * This method is used to update the Access Point configuration.
     *
     * @param privilegesDto
     * @return the service status class object with response status and payload.
     */
    @ApiOperation(value = "Update a privileges")
    @PutMapping("/auth/updatePrivileges")
    ServiceStatus<Object> updatePrivileges(@RequestBody List<PrivilegesMappingDTO> privilegesDto) {
        List<PrivilegesMapping> privileges = this.modelMapper.map(privilegesDto, ArrayList.class);
        return authService.updateListOfPrivileges(privileges);
    }
}
