Feature: Authenticated user with preference list use the option
  for find the closest spot

  Scenario: Authenticated User choose the closest spot
    Given I am authenticated user
    When I have select the park 'D' on spinner option
    And I select the option Find Me A Spot on dashboard auth menu
    Then I see the error message saying "No free spots available"