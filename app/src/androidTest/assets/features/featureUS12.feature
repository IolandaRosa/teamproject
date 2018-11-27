Feature: Authenticated user with preference list use the option
  for find a spot with preference list

  Scenario: Authenticated User with favourite spots list choose a spot by preference
    Given I am authenticated user
    When I select the option Find Me A Spot on dashboard auth menu
    Then I see that the spot returned from my list is the best rated free spot