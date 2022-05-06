package com.serve.controller;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.serve.dao.AppointmentRepository;
import com.serve.dao.PharmacyRepository;
import com.serve.dao.UserRepository;
import com.serve.entities.Appointment;
import com.serve.entities.Pharmacy;
import com.serve.entities.User;
import com.serve.helper.Message;









@Controller 
@RequestMapping("/user")
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Autowired
	private PharmacyRepository pharmacyRepository;
	
	@ModelAttribute
	public void addCommanData(Model m,Principal principal) {
		
		String userName=principal.getName();
		System.out.println("USERNAME "+userName);
		
		//get the user using username(Email)  
		
		User user = userRepository.getUserByUserName(userName);
		
		System.out.println("USER "+user);
		m.addAttribute("user", user);
		System.out.println(new Date());
//		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss"); 
//		Date date=new Date();
//		String strDate = formatter.format(date);
//		System.out.println(strDate);
	}
	
	
	
	@GetMapping("/index")
	public String dashboard(Model model, Principal principal) {
		
       model.addAttribute("title", "Dashboard");
		
		return "normal/user_dashboard";
		
	}
	
	
	// open add  form handler
		@GetMapping("/new-appointment")
		public String newAppointment(Model model ) {
			
			model.addAttribute("title", "New Appointment");
			model.addAttribute("appointment", new Appointment() );
			return "normal/new_Appointment";
		}
	
    // processing new appointment
		
	@PostMapping("/process-appointment")	
	public String bookAppointment(@ModelAttribute Appointment appointment,Principal principal, HttpSession session,Model model) {
		
		try {
			
			
			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);
			
			
			Date date=new Date();
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"); 
			
			String strDate = formatter.format(date);
			
			
//			SimpleDateFormat formatter1 = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a ");
//			Date date1 = formatter1.parse(appointment.getDate());
//			
//		
//			System.out.println("userDate in Date "+date1);
////			System.out.println(date2);
			
			int i=strDate.compareTo(appointment.getDate());
			
			System.out.println("currDate "+strDate);
			System.out.println("userDate "+appointment.getDate());
			
			System.out.println("I "+i);
			
			if(i>=1){
				
				throw new Exception("Choose a valid date");
				
			}
			
			
			appointment.setUser(user);
			user.getAppointments().add(appointment);
			this.userRepository.save(user);
			
			System.out.println("DATA "+appointment);
			System.out.println("Add to databases"); 
			
			
			session.setAttribute("message", new Message("Your appointment is booked !! ", "alert-success"));
			model.addAttribute("i", i);
			
		} catch (Exception e) {
			// TODO: handle exception
			
			System.out.println("ERROR "+e.getMessage());
			e.printStackTrace();
			
			//error message.....
			
			session.setAttribute("message", new Message("Something went wrong !! Try again.."+e.getMessage(), "alert-danger"));
			
		}
		
		
		
		return "normal/new_Appointment";
	}	
	
	//appointment history 
	
	@GetMapping("/show-appointment/{page}")
    public String showAppointment(@PathVariable("page") Integer page,Model model,Principal principal) {
    	
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		
		
		Pageable pageable = PageRequest.of(page, 3);
		
		Page<Appointment> appointment = this.appointmentRepository.findAppointmentsByUser(user.getId(), pageable);
		
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages",appointment.getTotalPages());
		model.addAttribute("appointment", appointment);
		model.addAttribute("title", "Appointment History");
    	
    	return "normal/show_appointment";
    }
	
	//pharmacy section
	
	@GetMapping("/new-pharmacy")
	public String addPharmacy(Model model) {
		
		model.addAttribute("title", "Pharmacy");
		model.addAttribute("pharmacy", new Pharmacy());
		return "normal/new_pharmacy";
	}
	
	@PostMapping("/process-pharmacy")
	public String processPharmacy(@ModelAttribute Pharmacy pharmacy,@RequestParam("profileImage") MultipartFile file,
			Principal principal,HttpSession session) {
		
		try {
			
			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);
			
			if(file.isEmpty()) {
				
				throw new Exception("Please upload an image of prescription");
			}
			
			else {
				
				pharmacy.setImgage(file.getOriginalFilename());
				File saveFile = new ClassPathResource("static/image").getFile();
				
				Path path= Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				
				Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image is uplaoded");
				
			}
			
			pharmacy.setUser(user);
			user.getPharmacies().add(pharmacy);
			this.userRepository.save(user);
			
			System.out.println("DATA "+pharmacy);
			System.out.println("Add to databases"); 
			
			//message success....
			
			session.setAttribute("message", new Message("Your Medicines are ordered !! ", "alert-success"));
			
			
		} catch (Exception e) {
			// TODO: handle exception
			
			System.out.println("ERROR "+e.getMessage());
			e.printStackTrace();
			
			//error message.....
			
			session.setAttribute("message", new Message("Something went wrong !!"+e.getMessage(), "alert-danger"));
			
		}
		
		
		
		return "normal/new_pharmacy";
	}
	
	@GetMapping("/show-pharmacy/{page}")
	public String showPharmacy(@PathVariable("page") Integer page,Model model,Principal principal) {
		
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);		
		
		Pageable pageable = PageRequest.of(page, 3);
		Page<Pharmacy> pharmacy = this.pharmacyRepository.findPharmaciesByUser(user.getId(), pageable);
		
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages",pharmacy.getTotalPages());
		model.addAttribute("pharmacy", pharmacy);
		model.addAttribute("title", "Pharmacy History");
		
		return "normal/show_pharmacy";
	}
	
	@GetMapping("/{Pid}/pharmacy")
	public String detailsPharmacy(@PathVariable("Pid") Integer Pid,Model model, Principal principal) {
		
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		
		Pharmacy id = this.pharmacyRepository.getById(Pid);
		
		String imgage = id.getImgage();
		String address = id.getAddress();
		
		
		if(user.getId()==id.getUser().getId()) {
			
			model.addAttribute("image", imgage);
			model.addAttribute("address", address);
			
		}
		
		System.out.println("Pid "+ Pid);
		model.addAttribute("title", "Pharmacy History "+Pid);
		return "normal/pharmacy_detail";
	}
	
	//open setting handler
	
	@GetMapping("/settings")
	public String opensetting(Model model) {
		
		model.addAttribute("title", "Setting");
		return "normal/settings";
	}
	
	//change password handler
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
			Principal principal,HttpSession session) {
		
		
		System.out.println("OLD PASSWORD "+oldPassword);
		System.out.println("NEW PASSWORD "+newPassword); 
		
		String name = principal.getName();
		
		User currentUser = this.userRepository.getUserByUserName(name);
		
		if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
		
			//change password
			
			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(currentUser);
			
			session.setAttribute("message1", new Message("Your password is sucessfully changed !!", "alert-success"));
		}
		else {
			
			session.setAttribute("message1", new Message("Please Enter correct old password !!", "alert-danger"));
			return "redirect:/user/settings";
			
		}
		
		return "redirect:/user/index";
	}
	
	
}	