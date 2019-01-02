Feature: Leave the Spot
  As an authenticated user I want to leave the spot that I am parked


  Scenario: Leave a Spot Adding that spot to favourits list (Happy Path)
    Given I am an authenticated user and I am leaving my spot
    And I see a message saing I want to leave "Do you want to leave your spot"
    And I select the option "Yes"
    And I see a screen to give a rate to that spot
    And I insert the rate value
    And The button to send rate
    And I see a message asking if I want to add that spot to my favourits list spots
    And I press the button to add to my favourites
    Then The spot is displayed free on the dashboard
    And I go to my favourits spots list
    And The spot is added to my favourits spots list