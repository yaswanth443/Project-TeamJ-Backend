package com.nutri.rest.service;

import com.nutri.rest.exception.EmailException;
import com.nutri.rest.model.OTP;
import com.nutri.rest.model.User;
import com.nutri.rest.repository.OTPRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.core.io.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class MailService {

  private final JavaMailSender mailSender;

  @Value("${support.email}")
  private String supportEmail;

    @Value("${reset.email.base.url}")
    private String resetBaseUrl;

    private final OTPRepository otpRepository;

  public MailService(JavaMailSender mailSender, OTPRepository otpRepository) {
    this.mailSender = mailSender;
    this.otpRepository = otpRepository;
  }

  public String sendResetMail(String email, String otp, User user) {
    String subject = "Nutri Eats password reset";
    String body = "<html><body><img src='cid:myLogo' style='display:block;margin:auto;'><br/><hr><br/><br/><br/>" +
            "Your OTP for Nutri Eats account verification is <b>"+otp+"</b>. This OTP is valid for the next 15 mins.<br/>OTP is confidential. Please do not share this with anyone.<br/><br/><br/>" +
            "</b></body></html>";
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper;
    try {
      helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
              StandardCharsets.UTF_8.name());
      helper.setTo(email);
      helper.setFrom(supportEmail);
      helper.setSubject(subject);

      Resource resource = new ClassPathResource("newlogo.png");
      File attachment = resource.getFile();
      FileSystemResource file = new FileSystemResource(attachment);

      helper.setText(body, true);
      helper.addInline("myLogo", file);
      mailSender.send(message);
      otpRepository.save(OTP.builder().userName(user.getUserName())
              .otp(otp)
              .createdDate(LocalDateTime.now())
              .expiryTime(LocalDateTime.now().plusMinutes(15))
              .build());
    } catch (MessagingException | IOException e) {
      throw new EmailException("Sending email for sendOrderConfirmationMailToCustomer failed for email : " + email);
    }
    return "success";
  }

  public String sendOrderConfirmationMailToCustomer(String email, String customerName, String restaurantName, String orderNumber) {
    String subject = "Nutri Eats order confirmation";
    String body = "<html><body><img src='cid:myLogo' style='display:block;margin:auto;'><br/><hr><br/>" +
            "Hi "+customerName+",<br/><br/>" +
            "Thanks for using Nutri Eats! <br/>Your order from <b>"+restaurantName+"</b> has been confirmed.<br/><br/><br/>" +
            "Order No: <b>#ORDER-"+orderNumber+"</b><br/>" +
            "Restaurant: <b>"+restaurantName+"</b></body></html>";
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper;
    try {
      helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
              StandardCharsets.UTF_8.name());
      helper.setTo(email);
      helper.setFrom(supportEmail);
      helper.setSubject(subject);

      Resource resource = new ClassPathResource("newlogo.png");
      File attachment = resource.getFile();
      FileSystemResource file = new FileSystemResource(attachment);

      helper.setText(body, true);
      helper.addInline("myLogo", file);
      mailSender.send(message);
    } catch (MessagingException | IOException e) {
      throw new EmailException("Sending email for sendOrderConfirmationMailToCustomer failed for email : " + email);
    }
    return "success";
  }

  public String sendOrderDeliveredMailToCustomer(String email, String customerName, String restaurantName, String orderNumber) {
    String subject = "Your Nutri Eats order has been delivered";
    String body = "<html><body><img src='cid:myLogo' style='display:block;margin:auto;'><br/><hr><br/>" +
            "Hi "+customerName+",<br/><br/>" +
            "Thanks for using Nutri Eats! <br/>Your order from <b>"+restaurantName+"</b> has been delivered.<br/><br/><br/>" +
            "Order No: <b>#ORDER-"+orderNumber+"</b><br/>" +
            "Restaurant: <b>"+restaurantName+"</b></body></html>";
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper;
    try {
      helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
              StandardCharsets.UTF_8.name());
      helper.setTo(email);
      helper.setFrom(supportEmail);
      helper.setSubject(subject);

      Resource resource = new ClassPathResource("newlogo.png");
      File attachment = resource.getFile();
      FileSystemResource file = new FileSystemResource(attachment);

      helper.setText(body, true);
      helper.addInline("myLogo", file);
      mailSender.send(message);
    } catch (MessagingException e) {
      throw new EmailException("Sending email for sendOrderDeliveredMailToCustomer failed for email : " + email);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return "success";
  }
}
