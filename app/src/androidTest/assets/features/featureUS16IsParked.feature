Feature: Park manually
  As an authenticated user I want to chose a spot on the dashboard and me able to spot there

  Scenario: User is already parked
    Given I am authenticated user
    And I am already parked
    When I press the "Park Manually" on the menu
    Then I see a message saying that I am already parked
    And I am on the dashboard auth