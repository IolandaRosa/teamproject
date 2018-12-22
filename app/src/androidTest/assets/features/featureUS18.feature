Feature: Leave the Spot
  As an authenticated user I want to leave the spot that I am parked

  Scenario: User selects no when asked if is leaving spot
    Given I am an authenticated user
    And I am parked
    And I select the option "Leave My Spot" on the menu
    And I see a dialog asking if I want to leave
    And I select the option "No"
    Then I am on the auth dashboard
    And I am parked
    And The spot is occupied

  Scenario: User rates the spot and add the spot to his favourites
    Given I am an authenticated user
    And I am parked
    And I select the option "Leave My Spot" on the menu
    And I see a dialog asking if I want to leave
    And I select the option "Yes"
    And I see an ecran where I can rate the spot, add to my favourites and close this ecran
    And I press a star
    And The button to send rate
    Then I see a text saying that the rate was sended
    Then I press the button to add to my favourites
    And I see a message saying that the spot is on my favourites list
    Then I press the close button
    And I am on the auth dashboard
    And I am not parked
    And The spot is free