Feature: An authenticated user can't report a incident

  Scenario: User doesn't write description
    Given I am an authenticated user
    When I press the menu option "Report an Incident"
    And I press the save button
    Then I see a error saying that description can't be empty

  Scenario: User doesn't choose a location
    Given I am an authenticated user
    When I press the menu option "Report an Incident"
    And I insert a description on the description field
    And I press the save button
    Then I see a error saying that I need to put a location


