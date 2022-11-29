package com.homemanagement.authentication.service.impl;


import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import com.homemanagement.authentication.service.UserAuthenticationService;
import com.homemanagement.constant.HomeManagementConstant;
import com.homemanagement.constant.HomeManagementKeyConstant;
import com.homemanagement.domain.CompanyMaster;
import com.homemanagement.domain.PrivilegesMapping;
import com.homemanagement.domain.Roles;
import com.homemanagement.domain.User;
import com.homemanagement.dto.UserDTO;
import com.homemanagement.dto.EmailVo;
import com.homemanagement.repositories.HomeExpedatureRepository;
import com.homemanagement.repositories.RoleRepository;
import com.homemanagement.repositories.UserDetailsDecorator;
import com.homemanagement.repositories.UserRepository;
import com.homemanagement.security.MongoUserDetailsManager;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;


class CheckRoles {
    private CheckRoles() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckRoles.class);

    public static boolean checkPrivilliges(UserDetailsDecorator currentUser, boolean checkUser) {
        MongoTemplate mongoTemplate = null;
        try (MongoClient mongoClient = new MongoClient(new MongoClientURI(HomeManagementConstant.MONGO_URL.getKey()))) {
            mongoTemplate = new MongoTemplate(mongoClient, HomeManagementConstant.PORTAL.getKey());
            List<Roles> roleList = mongoTemplate.findAll(Roles.class); for (Roles roles : roleList) {
                if (roles.getName().equals(currentUser.getRole())) {
                    LOGGER.info(" Current role is  :" + currentUser.getRole());
                    final Query query =
                            Query.query(Criteria.where(HomeManagementKeyConstant.USERID).is(currentUser.getUser().getId()));
                   List<PrivilegesMapping> privilegesList = mongoTemplate.find(query, PrivilegesMapping.class);
                    for (PrivilegesMapping privilliges : privilegesList) {
                        if (privilliges.getUser_id().equals(currentUser.getUser().getId())) {
                            checkUser = true;
                        } else {LOGGER.debug(" Permissions doesnot have perticular Role");}
                    }
                }
            }

        } catch (Exception eMsg) {
            LOGGER.error(" returns exception  : ".concat(eMsg.getMessage()));
        } return checkUser;
    }
}

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationServiceImpl.class);

    @Autowired
    private MongoUserDetailsManager userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    HomeExpedatureRepository expendatureRepository;


    @Autowired
    RoleRepository roleRepository;

    private boolean userIsAtLeastAdmin(UserDetailsDecorator currentUser) {
        List<Roles> roles = roleRepository.getAllRoleList(); for (Roles role : roles) {
            if (currentUser.getUser().getRole_id().equals(role.getId())) {
                if (role.getName().equals(HomeManagementConstant.ADMIN)) {
                    return true;
                } else {
                    return false;
                }
            }
        } return false;
    }

    private boolean userIsXadaAdmin(UserDetailsDecorator currentUser) {
        List<Roles> roles = roleRepository.getAllRoleList(); for (Roles role : roles) {
            if (currentUser.getUser().getRole_id().equals(role.getId())) {
                if (HomeManagementConstant.ADMIN.equals(role.getName())) {
                    return true;
                } else {
                    return false;
                }
            }
        } return false;
    }

    private boolean isAdminRequired(boolean adminRequiered, UserDetailsDecorator currentUser) {
        if (adminRequiered) {
            if (userIsAtLeastAdmin(currentUser)) {
                return true;
            }
        } else {return true;}
        return false;
    }
    @Override
    public boolean canAccessUser(Object obj) {
        boolean checkUser = false; if (obj.equals(HomeManagementConstant.ANONYMOUS_USER)) {
            return checkUser;
        } else {
            UserDetailsDecorator currentUser = (UserDetailsDecorator) obj;
            LOGGER.debug("Checking if user={} has access to user={}", currentUser);
            return userIsXadaAdmin(currentUser);
        }
    }

    public boolean canAccessUserWithUserDto(Object obj, UserDTO userDTO, @Nullable boolean adminRequiered) {
        boolean checkUser = false; if (obj.equals(HomeManagementConstant.ANONYMOUS_USER)) {
            return checkUser;
        } else {
            UserDetailsDecorator currentUser = (UserDetailsDecorator) obj;
            if (userIsXadaAdmin(currentUser) || userDTO.getUsername().equals(currentUser.getUsername())) {return true;}
            if (userDTO.getCompanyId() == null) {return checkUser;}
            if (currentUser.getUser().getCompany_id().equals(userDTO.getCompanyId())) {
                return isAdminRequired(adminRequiered, currentUser);
            } return checkUser;
        }
    }

    public boolean canAccessUserWithUser(Object obj, User user, @Nullable boolean adminRequiered) {
        boolean checkUser = false; if (obj.equals(HomeManagementConstant.ANONYMOUS_USER)) {
            return checkUser;
        } else {
            UserDetailsDecorator currentUser = (UserDetailsDecorator) obj;
            if (userIsXadaAdmin(currentUser) || user.getUsername().equals(currentUser.getUsername())) {return true;}
            if (user.getCompany_id() == null) {return checkUser;}
            if (currentUser.getUser().getCompany_id().equals(user.getCompany_id())) {
                return isAdminRequired(adminRequiered, currentUser);
            } return checkUser;
        }
    }

    public boolean canAccessUserWithUserName(Object obj, String userName, boolean adminRequiered) {
        boolean checkUser = false; if (obj.equals(HomeManagementConstant.ANONYMOUS_USER)) {
            return checkUser;
        } else {
            UserDetailsDecorator currentUser = (UserDetailsDecorator) obj;
            try {
                User user = (User) userService.loadUserByUsername(userName);
                if (userIsXadaAdmin(currentUser)) {
                    return true;
                }
                if (user == null) {
                    return checkUser;
                }
                if (currentUser.getUser().getCompany_id().equals(user.getCompany_id())) {
                    return isAdminRequired(adminRequiered, currentUser);
                }
            } catch (Exception e) {
                return true;
            }
            return checkUser;
        }
    }
    public boolean canAccessUserWithCompanyId(Object obj, String companyId) {
        boolean checkUser = false; if (obj.equals(HomeManagementConstant.ANONYMOUS_USER)) {
            return checkUser;
        } else {
            UserDetailsDecorator currentUser = (UserDetailsDecorator) obj;
            if (userIsXadaAdmin(currentUser)) {
                return true;
            }
            if (companyId == null) {
                return checkUser;
            }
            if (currentUser.getUser().getCompany_id().equals(companyId)) {
                return true;
            }
            return checkUser;
        }
    }

    public boolean canAccessUserWithCompanyMaster(Object obj, CompanyMaster companyMaster) {
        boolean checkUser = false;
        if (obj.equals(HomeManagementConstant.ANONYMOUS_USER)) {
            return checkUser;
        } else {
            UserDetailsDecorator currentUser = (UserDetailsDecorator) obj; try {
                User user = (User) userService.loadUserByUsername(companyMaster.getEmail());
                if (userIsXadaAdmin(currentUser)) {
                    return true;
                }
                if (user == null) {
                    return checkUser;
                }
                if (currentUser.getUser().getCompany_id().equals(user.getCompany_id())) {
                    return true;
                }
            } catch (Exception e) {
                return true;
            }

            return checkUser;
        }
    }

    public boolean canAccessUserWithEmail(Object obj, String email) {
        boolean checkUser = false; if (obj.equals(HomeManagementConstant.ANONYMOUS_USER)) {
            return checkUser;
        } else {
            UserDetailsDecorator currentUser = (UserDetailsDecorator) obj;
            if (userIsXadaAdmin(currentUser)) {return true;} try {
                User user = (User) userService.loadUserByUsername(email); if (user == null) {return checkUser;}
                if (currentUser.getUser().getCompany_id().equals(user.getCompany_id())) {
                    return true;
                }
            } catch (Exception e) {return true;} return checkUser;
        }
    }

    public boolean canAccessUserWithListUserDTO(Object obj, List<UserDTO> userDTOs) {
        boolean checkUser = false; if (obj.equals(HomeManagementConstant.ANONYMOUS_USER)) {
            return checkUser;
        } else {
            UserDetailsDecorator currentUser = (UserDetailsDecorator) obj;
            if (userIsXadaAdmin(currentUser)) {return true;} for (UserDTO userDTO : userDTOs) {
                if (!currentUser.getUser().getCompany_id().equals(userDTO.getCompanyId())) {
                    return checkUser;
                }
            } return true;
        }
    }

    public boolean canAccessUserWithEmailVO(Object obj, EmailVo emailVO) {
        boolean checkUser = false; if (obj.equals(HomeManagementConstant.ANONYMOUS_USER)) {
            return checkUser;
        } else {
            UserDetailsDecorator currentUser = (UserDetailsDecorator) obj;
            if (userIsXadaAdmin(currentUser)) {return true;}
            if (!currentUser.getUser().getUsername().equals(emailVO.getFromUser())) {
                return checkUser;
            } return false;
        }
    }

    public boolean canAccessUserWithUserId(Object obj, String id) {
        boolean checkUser = false; if (obj.equals(HomeManagementConstant.ANONYMOUS_USER)) {
            return checkUser;
        } else {
            UserDetailsDecorator currentUser = (UserDetailsDecorator) obj;
            if (userIsXadaAdmin(currentUser)) {return true;} Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                try {
                    if (!currentUser.getUser().getCompany_id().equals(user.get().getCompany_id())) {
                        return checkUser;
                    }
                } catch (Exception e) {
                    return true;
                }
            } return true;
        }
    }

    public boolean canAccessUserWithUserDTOSameUser(Object obj, UserDTO userDTO) {
        boolean checkUser = false; if (obj.equals(HomeManagementConstant.ANONYMOUS_USER)) {
            return checkUser;
        } else {
            UserDetailsDecorator currentUser = (UserDetailsDecorator) obj;
            if (userIsXadaAdmin(currentUser)) {
                return true;
            }
            if (!currentUser.getUser().getId().equals(userDTO.getUserId())) {
                return checkUser;
            }
            return true;
        }
    }

}
 