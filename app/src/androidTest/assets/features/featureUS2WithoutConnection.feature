Feature: Login for an anonymous user without internet connection

  Scenario: Anonymous User wants to log in
    Given I am an anonymous user
    When I am in the login screen
    And I see all the fields displayed and empty
    And I type my email on email field "test@test.test"
    And I type my password on password field "12345678"
    And I press the Login button in login activity
    Then I see an error message saying "No Internet connection"
