Feature: Authenticated user with preference list use the option
  for find a spot with preference list

  Scenario: Authenticated User with favourite spots list with all spots occupied choose a spot by preference
    Given I am authenticated user
    When I select the option Find Me A Spot on dashboard auth menu
    Then I see an error message displayed saying "All of your favourite spots are occupied"