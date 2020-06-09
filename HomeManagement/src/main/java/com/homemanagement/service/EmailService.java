/*
 * package com.homemanagement.service;
 * 
 * 
 * import java.io.IOException; import java.io.UnsupportedEncodingException;
 * 
 * import javax.mail.Message; import javax.mail.MessagingException; import
 * javax.mail.internet.InternetAddress; import javax.mail.internet.MimeMessage;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.beans.factory.annotation.Value; import
 * org.springframework.mail.SimpleMailMessage; import
 * org.springframework.mail.javamail.JavaMailSender; import
 * org.springframework.stereotype.Service;
 * 
 *//**
	 * <h1>EmailService</h1> this class is used to send mails to users
	 * 
	 * @author
	 * @version 1.0
	 */
/*
 * @Service("emailService") public class EmailService {
 * 
 * @Autowired private JavaMailSender javaMailSender;
 * 
 * @Value("${email.from}") String from;
 * 
 *//**
	 * this method is used to send mails to friends
	 * 
	 * @param to      -- friend name
	 * @param sub     -- subject of mail
	 * @param msgBody -- body of message
	 */
/*
 * public void sendEmail(String[] to, String sub, String msgBody){
 * 
 * 
 * try { MimeMessage msg= javaMailSender.createMimeMessage(); msg.setFrom(new
 * InternetAddress(from, "Do-Not-Reply")); msg.setSubject(sub); for(int
 * i=0;i<to.length;i++) { msg.addRecipients(Message.RecipientType.TO, to[i]); }
 * msg.setContent(msgBody, "text/html; charset=utf-8");
 * javaMailSender.send(msg); } catch (MessagingException |
 * UnsupportedEncodingException e) { e.printStackTrace(); } }
 *//**
	 * this method is used to send mails to friends
	 * 
	 * @param to      -- friend name
	 * @param sub     -- subject of mail
	 * @param msgBody -- body of message
	 *//*
		 * public void sendEmailResetPassword(String[] to, String sub, String msgBody){
		 * 
		 * SimpleMailMessage message = new SimpleMailMessage(); message.setFrom(from);
		 * message.setTo(to); message.setSubject(sub); message.setText(msgBody);
		 * javaMailSender.send(message); }
		 * 
		 * public void sendMailFromCmdLine(String fileName) { final String SENDMAIL =
		 * "sendmail -R hdrs -N never -t -v < ";
		 * 
		 * String command = SENDMAIL + fileName; try { Runtime r = Runtime.getRuntime();
		 * 
		 * Process p = r.exec(new String[]{"/usr/bin/", "-c", command}); p.waitFor();
		 * 
		 * } catch (InterruptedException ex) { //LOGGER.log(Level.FATAL,
		 * ex.getMessage()); } catch (IOException ex) { //LOGGER.log(Level.FATAL,
		 * ex.getMessage()); }
		 * 
		 * }
		 * 
		 * }
		 */