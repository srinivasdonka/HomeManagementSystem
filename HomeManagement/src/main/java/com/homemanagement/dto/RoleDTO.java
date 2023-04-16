package com.homemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class RoleDTO {
    String id;
    String name;
    String description;
}
