Feature: Login for an anonymous user

  Background: Anonymous User wants to log in
    Given I am an anonymous user
    When I am in the dashboard screen
    And I press the Login button in dashboard
    And I see the login screen
    Then I see all the fields displayed and empty

  Scenario: Login as an anonymous user (happy path)
    Given I type my email on email field "test@test.test"
    And I type my password on password field "12345678"
    And I press the Login button in login activity
    And I see the authenticated dashboard page

  Scenario: Login as an anonymous user with invalid email
    Given I introduce an invalid email format "test@"
    And I type my password on password field "12345678"
    And I press the Login button in login activity
    Then I see an error message in email field saying "The email must have a valid format"

  Scenario: Login as an anonymous user with not registered email
    Given I introduce an email that does not exist on database "empty@empty.empty"
    And I type my password on password field "12345678"
    And I press the Login button in login activity
    Then I see an error message saying "Please, register your credentials"

  Scenario: Login as an anonymous user with invalid password
    Given I introduce an email that does not exist on database "test@test.test"
    And I type my password on password field "12345"
    And I press the Login button in login activity
    Then I see an error message in password field saying "The password must have at least 8 characters"

  Scenario: Login as an anonymous user with empty email
    Given I type my password on password field "12345"
    And I press the Login button in login activity
    Then I see an error message in email field saying "The email can't be empty"

  Scenario: Login as an anonymous user with invalid password
    Given I introduce an email that does not exist on database "test@test.test"
    And I press the Login button in login activity
    Then I see an error message in password field saying "The password can't be empty"

  Scenario: Login as an anonymous user empty fiels
    Given I press the Login button in login activity
    Then I see an error message saying "The fields can't be empty"