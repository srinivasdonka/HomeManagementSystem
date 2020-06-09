/*
 * package com.homemanagement.domain;
 * 
 * import java.time.LocalDateTime;
 * 
 * import org.springframework.data.annotation.PersistenceConstructor; import
 * org.springframework.data.mongodb.core.mapping.Document;
 * 
 * 
 * @Document public class RegistrationUser {
 * 
 * 
 * 
 * private String id; private String username; private String designation;
 * private String status; private String register_link; private LocalDateTime
 * created_timestamp; private LocalDateTime upated_timestamp;
 * 
 * 
 * 
 * 
 * 
 * public RegistrationUser() { }
 * 
 * @PersistenceConstructor public RegistrationUser(final String id, final String
 * username, final String designation, final String status, final String
 * register_link, final LocalDateTime created_timestamp,LocalDateTime
 * upated_timestamp) { this.id = id; this.username = username; this.designation
 * = designation; this.created_timestamp = created_timestamp;
 * this.status=status; }
 * 
 * public String getId() { return id; }
 * 
 * public void setId(String id) { this.id = id; }
 * 
 * public String getUsername() { return username; }
 * 
 * public void setUsername(String username) { this.username = username; }
 * 
 * public String getDesignation() { return designation; }
 * 
 * public void setDesignation(String designation) { this.designation =
 * designation; }
 * 
 * public String getStatus() { return status; }
 * 
 * public void setStatus(String status) { this.status = status; }
 * 
 * public LocalDateTime getCreated_timestamp() { return created_timestamp; }
 * 
 * public void setCreated_timestamp(LocalDateTime created_timestamp) {
 * this.created_timestamp = created_timestamp; }
 * 
 * public LocalDateTime getUpated_timestamp() { return upated_timestamp; }
 * 
 * public void setUpated_timestamp(LocalDateTime upated_timestamp) {
 * this.upated_timestamp = upated_timestamp; }
 * 
 * public String getRegister_link() { return register_link; }
 * 
 * public void setRegister_link(String register_link) { this.register_link =
 * register_link; }
 * 
 * 
 * 
 * }
 */