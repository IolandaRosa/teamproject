Feature: Profile for an authenticated user

  Scenario: Authenticated User wants to see your Profile details
    Given I am an authenticated user
    When I am in the dashboard authenticated screen
    And I press the My Profile button
    And I am in the Profile screen
    And The name matches with "Maria Pt"
    And The email mathces with "maria@email.pt"
    And The Find Me a Spot Preferences is displayed
    Then The preferences shows "None"
