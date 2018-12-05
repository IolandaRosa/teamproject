Feature: Authenticated user can Find a Spot with Best Rated Value

  Scenario:  Authenticated user With BEST_RATED Preferences retrieve a best rated Spot
    Given I am an authenticated user
    When There are a list of spots not empy
    Then A find a spot by best rated preference returns the spot "TESTE-4"