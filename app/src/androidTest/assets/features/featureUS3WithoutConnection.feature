Feature: Register for an anonymous user without internet connection

  Scenario: Anonymous User wants to register
    Given I am an anonymous user
    When I am in the register screen
    And I see all the fields displayed and empty
    And I type my name "Jose" on name field
    And I type my email "jose@jose.com" on email field
    And I type my password "123456677" on password field
    And I type my confirmation password "123456677" on confirmation password field
    And I click the register button in the register screen
    Then I see an error message saying "No Internet connection"
