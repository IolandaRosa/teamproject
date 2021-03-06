Feature: Park manually
  As an authenticated user I want to chose a spot on the dashboard and me able to spot there


  Scenario: User doesn't park on the spot
    Given I am authenticated user
    When I press the "Park Manually" on the menu
    Then I see a dialog box saying "Please, select the spot where you want to park."
    And I select other marker on the map
    And I see a message asking "Do you want to park in the spot selected?"
    And I select select the option "No"
    And The spot status is free

  Scenario: User park on the spot
    Given I am authenticated user
    When I press the "Park Manually" on the menu
    Then I see a dialog box saying "Please, select the spot where you want to park."
    And I select a marker on the map
    And I see a message asking "Do you want to park in the spot selected?"
    And I select select the option "Yes"
    And The spot status is occupied

  Scenario: User is already parked
    Given I am authenticated user
    When I press the "Park Manually" on the menu
    Then I see a message saying that I am already parked