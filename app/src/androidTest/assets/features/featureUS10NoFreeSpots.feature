Feature: Authenticated user can Find a Spot with Best Rated Value

  Scenario:  Authenticated user With BEST_RATED Preferences with no spots free
    Given I am an authenticated user
    When I have no free spots avaiable
    And I have select the park 'A' on spinner option
    And I select the menu option "Find me a spot" dashboard authenticated
    Then I see the error message says "All spots are occupied"