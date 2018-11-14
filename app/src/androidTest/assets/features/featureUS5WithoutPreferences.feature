Feature: Profile for an authenticated user

  Scenario: Authenticated User wants to see your Profile details
    Given I am an anonymous user
    When I am in the dashboard screen
    And I press the sign up button
    And I type my email "maria@email.pt"
    And I type my password "12345678"
    And I press the Login Button
    And I press the My Profile button
    And I am in the Profile screen
    And The name matches with "Maria Pt"
    And The email mathces with "maria@email.pt"
    And The Find Me a Spot Preferences is displayed
    Then The preferences shows "None"
