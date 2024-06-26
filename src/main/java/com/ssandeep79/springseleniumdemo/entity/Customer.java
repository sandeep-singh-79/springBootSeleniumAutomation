package com.ssandeep79.springseleniumdemo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Customer {
    @Id
    private Integer id;
    private String fromCountry;
    private String toCountry;
    private LocalDate dob;
    private String firstName;
    private String lastName;
    @Column(name = "customer_email") private String email;
    private String phone;
    private String comments;
}
