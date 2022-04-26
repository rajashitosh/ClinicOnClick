package com.serve.dao;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.serve.entities.Pharmacy;


public interface PharmacyRepository extends JpaRepository<Pharmacy, Integer> {
	
	@Query("from Pharmacy as c where c.user.id =:userId")
	//currentPage page
	//contact Per page - 5
	
	public Page<Pharmacy> findPharmaciesByUser(@Param("userId") int userId, Pageable pageable);
	
//	public List<Pharmacy> findByNameContainingAndUser(String name, User user);

}
