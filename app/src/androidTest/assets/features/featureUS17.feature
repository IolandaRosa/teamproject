Feature: My Spot
  As an authenticated user I want to chose see the spot that I am parked

  Scenario: User is parked and want's to see the his spot
    Given I am an authenticated user
    And I am parked
    And I select the option "My spot" on the menu
    Then I am on the dashboard
    And I see only my spot marked on the map
