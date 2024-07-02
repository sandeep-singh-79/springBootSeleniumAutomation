@smoke
Feature: Visa Registration

  Scenario Outline: Submit visa registration form
    Given I am on the visa registration page
    When I enter relevant registration details - "<fromCountry>", "<toCountry>", "<dob>", "<first_name>", "<last_name>", "<email>", "<phone>", "<comments>"
    And I submit the form
    Then I should see a success message

    Examples:
      | fromCountry      | toCountry                   | dob        | first_name | last_name | email              | phone          | comments |
      | Isle of Man      | Mali                        | 2011-05-31 | Kraig      | Wiza      | Kraig@nobody.com   | 1-000-884-1373 |          |
      | Lithuania        | Mexico                      | 2001-01-01 | Houston    | Kertzmann | Houston@nobody.com | 284.864.6580   |          |
      | Somalia          | Greece                      | 2004-07-02 | Ruthie     | Stamm     | Ruthie@nobody.com  | 1-209-813-9712 |          |
      | Christmas Island | French Southern Territories | 2019-04-05 | Shonna     | Nolan     | Shonna@nobody.com  | (162) 387-0305 |          |
