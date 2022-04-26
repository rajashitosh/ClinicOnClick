package com.serve.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.serve.entities.Appointment;
import com.serve.entities.User;


public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
	
	@Query("from Appointment as c where c.user.id =:userId")
	//currentPage page
	//contact Per page - 5
	public Page<Appointment> findAppointmentsByUser(@Param("userId") int userId, Pageable pageable);
	
	public List<Appointment> findByNameContainingAndUser(String name, User user);

}
