package com.ssandeep79.springseleniumdemo.demo.utils;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Utility class for generating random test data
 * Extends JavaFaker functionality with domain-specific data generators
 */
@Component
public class RandomDataUtility {
    private final Faker faker;
    
    // Common domains for email generation
    private static final String[] EMAIL_DOMAINS = {
        "gmail.com", "yahoo.com", "outlook.com", "hotmail.com", "aol.com", 
        "protonmail.com", "icloud.com", "example.com", "testdomain.org"
    };
    
    // For generating phone numbers with country codes
    private static final String[] COUNTRY_CODES = {
        "+1", "+44", "+61", "+91", "+49", "+33", "+81", "+86", "+7", "+27"
    };
    
    public RandomDataUtility() {
        this.faker = new Faker();
    }
    
    /**
     * Gets the underlying Faker instance
     * @return Faker instance
     */
    public Faker getFaker() {
        return faker;
    }
    
    /**
     * Generates a random email with options for format
     * @param useRealName whether to use realistic names in the email
     * @param useDot whether to include dots in the email username
     * @return random email address
     */
    public String randomEmail(boolean useRealName, boolean useDot) {
        String domain = EMAIL_DOMAINS[ThreadLocalRandom.current().nextInt(EMAIL_DOMAINS.length)];
        
        if (useRealName) {
            String firstName = faker.name().firstName().toLowerCase().replaceAll("[^a-z]", "");
            String lastName = faker.name().lastName().toLowerCase().replaceAll("[^a-z]", "");
            
            if (useDot) {
                return firstName + "." + lastName + "@" + domain;
            } else {
                return firstName + lastName + "@" + domain;
            }
        } else {
            String username = faker.lorem().characters(5, 10).toLowerCase();
            int num = ThreadLocalRandom.current().nextInt(100, 999);
            return username + num + "@" + domain;
        }
    }
    
    /**
     * Generates a formatted phone number with country code
     * @param countryCode specific country code or null for random
     * @param formatted whether to include formatting characters
     * @return phone number string
     */
    public String randomPhoneNumber(String countryCode, boolean formatted) {
        String prefix = countryCode != null ? countryCode : 
                        COUNTRY_CODES[ThreadLocalRandom.current().nextInt(COUNTRY_CODES.length)];
        
        if (formatted) {
            return prefix + " " + faker.phoneNumber().phoneNumber();
        } else {
            String raw = faker.phoneNumber().phoneNumber();
            return prefix + raw.replaceAll("[^0-9]", "");
        }
    }
    
    /**
     * Generates a random credit card number for testing
     * @param valid whether to generate a valid (passes Luhn check) card number
     * @param cardType card type (visa, mastercard, amex) or null for random
     * @return credit card number
     */
    public String randomCreditCardNumber(boolean valid, String cardType) {
        String number;
        
        if (cardType == null || cardType.isEmpty()) {
            number = faker.finance().creditCard();
        } else {
            switch (cardType.toLowerCase()) {
                case "visa":
                    number = "4" + faker.number().digits(15);
                    break;
                case "mastercard":
                    number = "5" + faker.number().digit() + faker.number().digits(14);
                    break;
                case "amex":
                    number = "3" + (ThreadLocalRandom.current().nextBoolean() ? "4" : "7") + faker.number().digits(13);
                    break;
                default:
                    number = faker.finance().creditCard();
            }
        }
        
        if (valid) {
            // Ensure it passes Luhn check if needed
            char[] digits = number.toCharArray();
            int checkDigit = calculateLuhnCheckDigit(number.substring(0, number.length() - 1));
            digits[digits.length - 1] = Character.forDigit(checkDigit, 10);
            number = new String(digits);
        }
        
        return number;
    }
    
    /**
     * Calculates Luhn check digit for credit card validation
     */
    private int calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = false;
        
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Character.digit(number.charAt(i), 10);
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        
        return (10 - (sum % 10)) % 10;
    }
    
    /**
     * Generates a random password with customizable complexity
     * @param length password length
     * @param includeSpecial whether to include special characters
     * @param includeDigits whether to include digits
     * @param includeMixedCase whether to include both upper and lower case
     * @return random password
     */
    public String randomPassword(int length, boolean includeSpecial, boolean includeDigits, boolean includeMixedCase) {
        if (length < 6) {
            length = 6; // Enforce minimum length for security
        }
        
        StringBuilder characterPool = new StringBuilder("abcdefghijklmnopqrstuvwxyz");
        
        if (includeMixedCase) {
            characterPool.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        }
        
        if (includeDigits) {
            characterPool.append("0123456789");
        }
        
        if (includeSpecial) {
            characterPool.append("!@#$%^&*()-_=+[]{}|;:,.<>?");
        }
        
        return ThreadLocalRandom.current()
                .ints(length, 0, characterPool.length())
                .mapToObj(i -> String.valueOf(characterPool.charAt(i)))
                .collect(Collectors.joining());
    }
    
    /**
     * Generates a random date of birth
     * @param minAge minimum age in years
     * @param maxAge maximum age in years
     * @return Date object representing the birth date
     */
    public Date randomDateOfBirth(int minAge, int maxAge) {
        if (minAge < 0) minAge = 0;
        if (maxAge < minAge) maxAge = minAge + 1;
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -minAge);
        Date maxDate = cal.getTime();
        
        cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -maxAge);
        Date minDate = cal.getTime();
        
        return faker.date().between(minDate, maxDate);
    }
    
    /**
     * Generates random postal/zip code based on country format
     * @param countryCode ISO country code (US, UK, CA, etc.)
     * @return formatted postal code
     */
    public String randomPostalCode(String countryCode) {
        if (countryCode == null) {
            return faker.address().zipCode();
        }
        
        switch (countryCode.toUpperCase()) {
            case "US":
                return faker.address().zipCode();
            case "UK":
                return faker.regexify("[A-Z]{1,2}[0-9][0-9A-Z]? [0-9][A-Z]{2}");
            case "CA":
                return faker.regexify("[A-Z][0-9][A-Z] [0-9][A-Z][0-9]");
            case "AU":
                return faker.number().digits(4);
            default:
                return faker.address().zipCode();
        }
    }
    
    /**
     * Generates a random UUID string
     * @return UUID string
     */
    public String randomUUID() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Generates random text with a specific number of paragraphs
     * @param paragraphs number of paragraphs to generate
     * @return text containing the specified number of paragraphs
     */
    public String randomText(int paragraphs) {
        return faker.lorem().paragraphs(paragraphs).stream()
                .collect(Collectors.joining("\n\n"));
    }
    
    /**
     * Generates random hex color code
     * @param includeHash whether to include # prefix
     * @return hex color code
     */
    public String randomColor(boolean includeHash) {
        String hexColor = faker.color().hex();
        return includeHash ? hexColor : hexColor.substring(1);
    }
    
    /**
     * Generates a random item from a provided list
     * @param items list of items to choose from
     * @param <T> type of items
     * @return randomly selected item
     */
    public <T> T randomItem(List<T> items) {
        if (items == null || items.isEmpty()) {
            return null;
        }
        return items.get(ThreadLocalRandom.current().nextInt(items.size()));
    }
    
    /**
     * Generates a random subset of items from a provided list
     * @param items list of items to choose from
     * @param count number of items to select
     * @param <T> type of items
     * @return list containing randomly selected items
     */
    public <T> List<T> randomSubset(List<T> items, int count) {
        if (items == null || items.isEmpty() || count <= 0) {
            return Collections.emptyList();
        }
        
        if (count >= items.size()) {
            return new ArrayList<>(items);
        }
        
        List<T> shuffled = new ArrayList<>(items);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, count);
    }
    
    /**
     * Generates a random IP address
     * @param ipv6 whether to generate IPv6 instead of IPv4
     * @return IP address string
     */
    public String randomIPAddress(boolean ipv6) {
        if (ipv6) {
            return faker.internet().ipV6Address();
        } else {
            return faker.internet().ipV4Address();
        }
    }
    
    /**
     * Generates a random company name with optional suffix
     * @param includeSuffix whether to include company suffix (Inc, LLC, etc.)
     * @return company name
     */
    public String randomCompanyName(boolean includeSuffix) {
        if (includeSuffix) {
            return faker.company().name();
        } else {
            return faker.company().name().replaceAll(" (Inc|LLC|Ltd|Group|Partners)$", "");
        }
    }
    
    /**
     * Generates a random username
     * @param minLength minimum username length
     * @param maxLength maximum username length
     * @param allowSpecialChars whether to allow special characters
     * @return random username
     */
    public String randomUsername(int minLength, int maxLength, boolean allowSpecialChars) {
        if (minLength < 3) minLength = 3;
        if (maxLength < minLength) maxLength = minLength + 1;
        
        int length = ThreadLocalRandom.current().nextInt(minLength, maxLength + 1);
        
        if (allowSpecialChars) {
            return faker.regexify("[a-z0-9_-]{" + length + "}");
        } else {
            return faker.regexify("[a-z0-9]{" + length + "}");
        }
    }
    
    /**
     * Generates a random product SKU code
     * @param prefix optional prefix for the SKU
     * @return SKU code
     */
    public String randomSKU(String prefix) {
        String sku = faker.regexify("[A-Z0-9]{8}");
        return prefix != null ? prefix + "-" + sku : sku;
    }
}
