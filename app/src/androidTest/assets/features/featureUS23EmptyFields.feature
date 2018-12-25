Feature: An authenticated user can't report a incident

  Scenario: User chooses a spot
    Given I am an authenticated user
    When I press the menu option "Report an Incident"
    And I press the save button
    Then I see a error saying that description can't be empty


