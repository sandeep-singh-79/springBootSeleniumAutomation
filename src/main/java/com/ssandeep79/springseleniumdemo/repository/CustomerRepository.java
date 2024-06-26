package com.ssandeep79.springseleniumdemo.repository;

import com.ssandeep79.springseleniumdemo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findByFirstNameStartingWith (String firstName);
    List<Customer> findByDobBetween (LocalDate start, LocalDate end);
}
