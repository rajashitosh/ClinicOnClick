package com.serve.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.serve.dao.UserRepository;
import com.serve.entities.User;
import com.serve.service.EmailService;

@Controller
public class ForgotController {

	Random random = new Random(1000);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	//email id form handler
	@GetMapping("/forgot")
	public String openEmailForm(Model model) {
		
		model.addAttribute("title", "Forgot Password");
		return "forgot_email_form";
	}
   
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email,HttpSession session) {
		
		System.out.println("EMAIL "+email);
		
		//generating otp of 4 digit
		
		
		
		int otp = random.nextInt(999999);
		
		System.out.println("OTP "+otp);
		
		String subject="OTP from ServeAppointment";
		String message=""
		                +"<div style='border: 4px soild #e2e2e2; padding:20px'>"
				        +"<h1>"
		                +"OTP is "
				        +"<b>"+otp
				        +"</n>"
				        +"</h1>"
				        +"</div>";
		String to=email;
		
		boolean flag = this.emailService.sendEmail(subject, message, to);
		
		if(flag) {
			
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			return "verify_otp";
		}
		else {
			
			session.setAttribute("message", "Check Your email id !!");
			
			return "forgot_email_form";
		}
		
	}
	
	//verify otp
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp,HttpSession session) {
		
		int myOtp =(int)session.getAttribute("myotp");
		String email= (String)session.getAttribute("email");
		
		if(myOtp==otp) {
			
			User user = this.userRepository.getUserByUserName(email);
			if(user==null) {
				
				session.setAttribute("message", "User does not exist with this email !!");
				
				return "forgot_email_form";
				
			}
			else {
				
				
			}
			
			return "password_change_form";
		}
		else {
			
			session.setAttribute("message", "You have entered wrong otp !!");
			return "verify_otp";
		}
		
		
	}
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newpassword") String newpassword,HttpSession session) {
		
		String email= (String)session.getAttribute("email");
		User user = this.userRepository.getUserByUserName(email);
		
		user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
		
		this.userRepository.save(user);
		
		return "redirect:/signin?change=password change successfully..";
	}
}
