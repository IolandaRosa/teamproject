Feature: Authenticated user can Find a Spot with Best Rated Value

  Background: Authenticated user With BEST_RATED Preferences
    Given I am an authenticated user
    Then I have select the park 'A' on spinner option


  Scenario:  Authenticated user With BEST_RATED Preferences retrieve a best rated Spot

    Given I select the menu option "Find me a spot" on dashboard authenticated
    Then A spot "TESTE-2" is selected

  Scenario:  Authenticated user With BEST_RATED Preferences with no spots free
    Given I have no free spots avaiable
    When I select the menu option "Find me a spot" dashboard authenticated
    Then I see the error message says "All spots are occupied"