@google
Feature: Google Search

  Scenario Outline: Search for '<keyword>'
    Given I am on Google home page
    When I search for "<keyword>" keyword
    Then I should see at least <count> results

    Examples:
      | keyword  | count |
      | Spring   | 2     |
      | Java     | 5     |
      | Selenium | 7     |