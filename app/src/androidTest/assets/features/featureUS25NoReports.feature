Feature: Updated Profile for an authenticate user
  As a user I want to update my profile information

  Scenario: There's no reports
    Given I am an authenticated user
    And There's no reports
    When I press the menu option "Incidents Reports List"
    Then I see a message saying that the're no reports to show