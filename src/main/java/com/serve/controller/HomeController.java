package com.serve.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.serve.dao.ContactRepository;
import com.serve.dao.UserRepository;
import com.serve.entities.Contact;
import com.serve.entities.User;
import com.serve.helper.Message;


@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder; 
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@GetMapping("/")
	public String home(Model model) {
		
		model.addAttribute("title", "Home - ServeAppointment");
		
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model model) {
		
		model.addAttribute("title", "About - ServeAppointment");
		
		return "about";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
	  
		model.addAttribute("title", "Register - ServeAppointment");
		model.addAttribute("user", new User());
	
		return "signup";
	}
	
	@PostMapping("/do_register")
	public String registerUser( @Valid @ModelAttribute("user") User user,BindingResult result,@RequestParam(value = "agreement", defaultValue="false") 
	boolean agreement,Model model,HttpSession session) {
		
		try {
			  
			if(!agreement)
			{
				System.out.println("You have not agreed the terms and condition");
				throw new Exception("You have not agreed the terms and condition");
			}
			
			if(result.hasErrors())
			{
				System.out.println("ERROR"+result.toString());
				model.addAttribute("user", user);
				return "signup";				
			}
			
			user.setRole("ROLE_USER");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		
			
			System.out.println("Agreement "+agreement);
			System.out.println("USER "+user);
			
			User result1 =this.userRepository.save(user);
			
			model.addAttribute("user", new User());
			
			session.setAttribute("message", new Message("Successfully Registered", "alert-success"));
			return "signup";
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something Went wrong !!"+e.getMessage(), "alert-danger"));
			return "signup";
		}
		
		
	}
	
	
	@GetMapping("/signin")
	public String customLogin(Model model)
	{
		model.addAttribute("title", "Login - ServeAppointment");
		return "login";
	}
	
	@PostMapping("/do_contact")
	public String contactUs(@ModelAttribute("conatct") Contact contact) {
		
		Contact contact1=this.contactRepository.save(contact);
		
		System.out.println(contact1);
		
		return "home";
	}
	
	@GetMapping("/find-doctor")
	public String findDoctor(Model model) {
		
		model.addAttribute("title", "Find Doctor - ServeAppointment");
		return "find_doctor";
	}
}
