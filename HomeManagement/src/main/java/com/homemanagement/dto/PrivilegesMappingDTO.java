package com.homemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class PrivilegesMappingDTO {
    String id;
    String name;
    String value;
    String role_id;
    String user_id;
    String privilegeId;
}
