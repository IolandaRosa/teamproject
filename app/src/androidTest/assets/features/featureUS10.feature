Feature: Authenticated user can Find a Spot with Best Rated Value

  Scenario:  Authenticated user With BEST_RATED Preferences retrieve a best rated Spot
    Given I am an authenticated user
    When I have select the park 'A' on spinner option
    And I select the menu option "Find me a spot" on dashboard authenticated
    Then A spot "TESTE-2" is selected