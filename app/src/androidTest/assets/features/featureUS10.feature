Feature: Authenticated user can Find a Spot with Best Rated Value

  Background: Authenticated user can Find a Spot with Best Rated Value
#    Given I am an authenticated user
#    When I select the menu option "Find me a spot" on dashboard authenticated
#    And I see the Google Maps with my Navigation for the Spot

  Scenario:  Authenticated user With BEST_RATED Preferences retrive a best rated Spot
    Given I am an authenticated user
    And I have BEST_RATED preferences
    When I select the menu option "Find me a spot" on dashboard authenticated
    Then A spot "TESTE-2" is selected

#
#    Given I am an authenticated user 
#    And I don't have a Find me a Spot preference
#    When I press the Find Me a Spot on the menu 
#    And I selected the option The best rated spot available
#    Then I see the indications on screen