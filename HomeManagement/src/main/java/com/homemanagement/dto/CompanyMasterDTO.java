package com.homemanagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyMasterDTO {
    String id;
    String name;
    String address;
    String status;
    Integer is_SI;
    Integer no_of_users;
    Integer no_of_active_users;
    Integer no_of_devices;
    String email;
    String created_date;
    String last_updated;
    String membership;
}
