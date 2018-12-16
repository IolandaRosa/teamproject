Feature: My Spot Not Parked
  As an authenticated user I want to chose see the spot that I am parked

  Scenario: User is not parked and selects the option My Spot
    Given I am an authenticated user
    And I am not parked
    And I select the option "My spot" on the menu
    Then I see a message saying that I am not parked

