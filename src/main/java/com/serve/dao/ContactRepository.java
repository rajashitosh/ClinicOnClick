package com.serve.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.serve.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

}
