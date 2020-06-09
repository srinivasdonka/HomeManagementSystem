/*
 * package com.homemanagement.security.config;
 * 
 * 
 * import java.util.Properties;
 * 
 * import org.springframework.beans.factory.annotation.Value; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.mail.javamail.JavaMailSender; import
 * org.springframework.mail.javamail.JavaMailSenderImpl;
 * 
 * @Configuration public class EmailConfig {
 * 
 * @Value("${email.protocol}") private String protocol;
 * 
 * @Value("${email.starttls.enable}") private String starttls;
 * 
 * @Value("${email.debug}") private String debug;
 * 
 * @Value("${email.auth}") private String auth;
 * 
 * @Value("${email.ssl.enable}") private String ssl;
 * 
 * @Value("${email.host}") private String host;
 * 
 * @Value("${email.port}") private Integer port;
 * 
 * @Value("${email.user}") private String user;
 * 
 * @Value("${email.pass}") private String password;
 * 
 * @Bean public JavaMailSender javaMailSender() {
 * 
 * JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
 * 
 * javaMailSender.setHost(host); javaMailSender.setPort(port);
 * javaMailSender.setUsername(user); javaMailSender.setPassword(password);
 * 
 * javaMailSender.setJavaMailProperties(getMailProperties());
 * 
 * return javaMailSender; }
 * 
 * private Properties getMailProperties() { Properties properties = new
 * Properties(); properties.setProperty("mail.transport.protocol", protocol);
 * properties.setProperty("mail.smtp.auth", auth);
 * properties.setProperty("mail.smtp.starttls.enable", starttls);
 * properties.setProperty("mail.debug", debug);
 * 
 * properties.setProperty("mail.smtp.auth", auth);
 * properties.setProperty("mail.smtp.ssl.enable", ssl);
 * 
 * properties.setProperty("mail.smtp.host", smtpPort);
 * properties.setProperty("mail.smtp.port", smtpPort);
 * properties.setProperty("mail.smtp.socketFactory.port", smtpPort);
 * properties.setProperty("mail.smtp.socketFactory.class", socketFactory);
 * 
 * return properties; } }
 */