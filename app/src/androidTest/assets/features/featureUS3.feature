Feature: Register as an anonymous user

  Background: Anonymous User wants to register
    Given I am an anonymous user
    When I am in the login screen
    And I press the register button in the login screen
    And  I see the register screen
    Then  I see all the fields displayed and empty


  Scenario: Register as an anonymous user (happy path)
    Given I type my name "Jose" on name field
    When I type my email "jose@jose.com" on email field
    And I type my password "123456677" on password field
    And I type my confirmation password "123456677" on confirmation password field
    And I click the register button in the register screen
    And I am registered with success
    And A new user whith email "jose@jose.com" is added on database
    Then I see the autheticated user dashboard


  Scenario: Register as an anonymous user already exists
    Given I type my name "Test" on name field
    When I type my email "test@test.test" on email field
    And I type my password "123456678" on password field
    And I type my confirmation password "123456677" on confirmation password field
    And I click the register button in the register screen
    And I see the error message on screen "You are already registered"
    Then The application assert that no user was authenticated and added on database


  Scenario: Register as an anonymous user with empty fields
    When I click the register button in the register screen
    When I see the error message on screen "The fields can't be empty"
    Then The application assert that no user was authenticated and added on database


  Scenario: Register as an anonymous user with invalid email format
    Given I type my name "Jose" on name field
    When I type my email "josejose.com" on email field
    And I type my password "123456677" on password field
    And I type my confirmation password "123456677" on confirmation password field
    And I click the register button in the register screen
    And I see the error message "The email must have a valid format" on the email field
    Then The application assert that no user was authenticated and added on database

  Scenario: Register as an anonymous user with invalid name (only digits)
    Given I type my name with numbers "123" on name field
    When I type my email "jose@jose.com" on email field
    And I type my password "123456677" on password field
    And I type my confirmation password "123456677" on confirmation password field
    And I click the register button in the register screen
    And I see the error message "The name can't contain only digits" on the name field
    Then The application assert that no user was authenticated and added on database

  Scenario: Register as an anonymous user with empty name
    When I type my email "jose@jose.com" on email field
    And I type my password "123456677" on password field
    And I type my confirmation password "123456677" on confirmation password field
    And I click the register button in the register screen
    And I see the error message "The name can't be empty" on the name field
    Then The application assert that no user was authenticated and added on database

  Scenario: Register as an anonymous user with empty email
    Given I type my name "Jose" on name field
    When I type my password "123456677" on password field
    And I type my confirmation password "123456677" on confirmation password field
    And I click the register button in the register screen
    And I see the error message "The email can't be empty" on the email field
    Then The application assert that no user was authenticated and added on database


  Scenario: Register as an anonymous user with empty password
    Given I type my name "Jose" on name field
    When I type my email "jose@jose.com" on email field
    And I type my confirmation password "123456677" on confirmation password field
    And I click the register button in the register screen
    And I see the error message "The password can't be empty" on the password field
    Then The application assert that no user was authenticated and added on database

  Scenario: Register as an anonymous user with empty password confirmation
    Given I type my name "Jose" on name field
    When I type my email "jose@jose.com" on email field
    And I type my password "123456677" on password field
    And I click the register button in the register screen
    And II see the error message "The confirmation password can't be empty" on the confirmation password field
    Then The application assert that no user was authenticated and added on database

  Scenario: Register as an anonymous user with wrong password confirmation
    Given I type my name "Jose" on name field
    When I type my email "jose@jose.com" on email field
    And I type my password "123456677" on password field
    And I type my confirmation password "123456678" on confirmation password field
    And I click the register button in the register screen
    And I see the error message "The confirmation password must be equal to password field" on the confirmation password field
    Then The application assert that no user was authenticated and added on database