Feature: Logout of a user

  Scenario: As an authenticated user I want to logout
    Given I am an authenticated user
    When I am in the authenticated dashboard page
    And I press the hamburguer button
    And I press the logout button
    And I am in the anonymous dashboard page
    And I am an anonymous user