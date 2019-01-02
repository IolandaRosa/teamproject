Feature: Leave the Spot
  As an authenticated user I want to leave the spot that I am parked

  Scenario: Leave My Spot Automatically Without Adding the Spot to Favourits List
    Given I am an authenticated user and I am leaving my spot
    And I see a message saing I want to leave "Do you want to leave your spot"
    And I select the option "Yes"
    And I see a screen to give a rate to that spot
    And I insert the rate value
    And The button to send rate
    Then The spot is displayed free on the dashboard


