package com.ssandeep79.springseleniumdemo.visa;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ssandeep79.springseleniumdemo.SpringBaseTestNGTest;
import com.ssandeep79.springseleniumdemo.entity.Customer;
import com.ssandeep79.springseleniumdemo.page.visa.VisaRegistrationPage;
import com.ssandeep79.springseleniumdemo.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserVisaTest extends SpringBaseTestNGTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VisaRegistrationPage visaRegistrationPage;


    @Test
    public void testUserVisaRecordSize () {
        assertEquals(customerRepository.findAll().size(), 100);
    }

    @Test
    public void testForSpecificUserVisaRecord () {
        assertTrue(customerRepository.findById(85).isPresent());
        assertEquals(customerRepository.findById(85).get().getFirstName(), "Andrea");
    }

    @Test (dataProvider = "getCustomerData")
    public void testVisaPage (List<Customer> customers) {
        customers.forEach(customer -> {
            log.info("Processing customer #{}: {} {}", customer.getId(), customer.getFirstName(), customer.getLastName());
            visaRegistrationPage.goTo();
            assertTrue(visaRegistrationPage.isAt());
            visaRegistrationPage.setName(customer.getFirstName(), customer.getLastName());
            visaRegistrationPage.setCountryFromAndTo(customer.getFromCountry(), customer.getToCountry());
            visaRegistrationPage.setBirthDate(customer.getDob());
            visaRegistrationPage.setContactDetails(customer.getEmail(), customer.getPhone());
            visaRegistrationPage.setComments(customer.getComments());

            visaRegistrationPage.submit();

            String confirmationNumber = visaRegistrationPage.getConfirmationNumber();
            log.info("visa registration confirmation number: {}", confirmationNumber);
            assertTrue(StringUtils.isNotEmpty(confirmationNumber));
        });
    }

    @DataProvider
    public Object[] getCustomerData () {
        return new Object[] {
            customerRepository.findAll().stream().limit(3).toList(),
            customerRepository.findByFirstNameStartingWith("Mi").stream().limit(3).toList(),
            customerRepository.findByDobBetween(LocalDate.of(1990, 1, 1), LocalDate.of(1995, 1, 1)).stream().limit(3).toList()
        };
    }
}