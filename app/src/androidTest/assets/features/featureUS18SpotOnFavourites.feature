Feature: Leave the Spot
  As an authenticated user I want to leave the spot that I am parked

  Scenario: Spot is already on the user favourites list
    Given I am an authenticated user
    And I am parked
    And The spot is already on my favourites list
    And I select the option "Leave My Spot" on the menu
    And I see a dialog asking if I want to leave
    And I select the option "Yes"
    Then I see an ecran where I can rate the spot and close this ecran
    And I don't see the button to add to my favourites
    And I see a message saying that the spot is already on my favourites list
    And I am not parked
    And The spot is free