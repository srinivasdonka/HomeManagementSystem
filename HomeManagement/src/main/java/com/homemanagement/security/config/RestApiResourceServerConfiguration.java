package com.homemanagement.security.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class RestApiResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    public static final String RESOURCE_ID = "restservice";
    public static final String ROLE_ID = "USER";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
		 /* UserController services */
		.antMatchers(HttpMethod.POST, "auth/createRole").hasRole(ROLE_ID)
        .antMatchers(HttpMethod.PUT, "auth/updateRole").hasRole(ROLE_ID)
        .antMatchers(HttpMethod.GET, "auth/getRoles/**").hasRole(ROLE_ID)
        
        .antMatchers(HttpMethod.POST, "auth/addPrivileges/**").hasRole(ROLE_ID)
        .antMatchers(HttpMethod.PUT, "auth/updatePrivileges").hasRole(ROLE_ID)
        .antMatchers(HttpMethod.GET, "auth/getPrivilegesByRole/**").hasRole(ROLE_ID)
        
        .antMatchers(HttpMethod.POST, "user/**").hasRole(ROLE_ID)
        .antMatchers(HttpMethod.GET, "user/**").hasRole(ROLE_ID)
        .antMatchers(HttpMethod.GET, "user/**").hasRole(ROLE_ID)
        
        .antMatchers(HttpMethod.POST, "deviceconfiguration/**").hasRole(ROLE_ID)
        .antMatchers(HttpMethod.PUT, "deviceconfiguration/**").hasRole(ROLE_ID)
        .antMatchers(HttpMethod.GET, "deviceconfiguration/**").hasRole(ROLE_ID)
        
        .antMatchers(HttpMethod.POST, "device/**").hasRole(ROLE_ID)
        .antMatchers(HttpMethod.PUT, "device/**").hasRole(ROLE_ID)
        .antMatchers(HttpMethod.GET, "device/**åß").hasRole(ROLE_ID);
        
       
    }

}
