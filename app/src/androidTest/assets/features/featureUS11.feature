Feature: Authenticated user with preference list use the option
  for find the closest spot

  Scenario: Authenticated User with favourite spots list choose the clsoest spot
    Given I am authenticated user
    When I select the option Find Me A Spot on dashboard auth menu
    Then I see that the spot returned from my list is the closest spot