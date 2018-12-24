Feature: Leave the Spot
  As an authenticated user I want to leave the spot that I am parked


  Scenario: Cancel Leave a Spot Automatically (Happy Path)
    Given I am an authenticated user and I am leaving my spot
    And I see a message saing I want to leave "Do you want to leave your spot"
    And I select the option "No"
    Then I am on the auth dashboard
    And I am parked
    And The spot is occupied