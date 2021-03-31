package com.paymybuddy.moneytransferapp.repository;

import com.paymybuddy.moneytransferapp.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
}
