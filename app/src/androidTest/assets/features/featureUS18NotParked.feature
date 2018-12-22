Feature: Leave the Spot
  As an authenticated user I want to chose see the spot that I am parked

  Scenario: User is not parked and selects the option Leave the Spot
    Given I am an authenticated user
    And I am not parked
    And I select the option "Leave My Spot" on the menu
    Then I see a message saying that I am not parked