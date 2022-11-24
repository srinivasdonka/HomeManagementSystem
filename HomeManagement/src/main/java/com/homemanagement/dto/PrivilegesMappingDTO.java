package com.homemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegesMappingDTO {
    private String id;
    private String name;
    private String value;
    private String roleId;
    private String userId;
    private String privilegeId;
}
