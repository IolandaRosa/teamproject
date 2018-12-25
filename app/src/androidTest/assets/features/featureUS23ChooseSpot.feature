Feature: An authenticated user can report a incident

  Scenario: User chooses a spot
    Given I am an authenticated user
    When I press the menu option "Report an Incident"
    And I insert the description on the description field
    And I see a button to get my current location and to choose a park and spot
    And I press the button to choose a park and spot
    And I don't see the button to get my current location
    And I see the spinners to choose a park and a spot
    And I choose a park on the spinner
    And I choose a spot on the spinner
    And I press the save button
    Then I see a message saying my incident was reported with success