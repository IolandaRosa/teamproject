Feature: Leave the Spot
  As an authenticated user I want to leave the spot that I am parked

  Scenario: User doens't rate the spot but add the spot to his favourites
    Given I am an authenticated user
    And I am parked
    And I select the option "Leave My Spot" on the menu
    And I see a dialog asking if I want to leave
    And I select the option "Yes"
    And I see an ecran where I can rate the spot, add to my favourites and close this ecran
    Then I press the button to add to my favourites
    And The spot is on my list of favourites
    And I am not parked
    And The spot is free


