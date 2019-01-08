# new feature
# Tags: optional

Feature: An authenticated user can see the occupation rate evolution displayed

  Scenario: An authenticated user can see the occupation rate during time (happy path)
    Given I am an authenticated user
    When I press the menu option "Occupation Rate During Time"
    And I insert the initial date value "12/11/2018"
    And I insert the final date value "12/12/2018"
    And I press the button with text "Show Occupation Rate Evolution On Period"
    Then I see the graphical information displayed

  Scenario: An authenticated user can see the occupation rate during time (happy path)
    Given I am an authenticated user
    When I press the menu option "Algorithm Performance"
    And I press the button to see graphical information
    And I insert the initial date value "12/11/2018"
    And I insert the final date value "12/12/2018"
    And I press the button with text "Show Occupation Rate Evolution On Period"
    Then I see the graphical information displayed

  Scenario: An authenticated user wants to see graphical information with empty date fields
    Given I am an authenticated user
    When I press the menu option "Occupation Rate During Time"
    And I press the button with text "Show Occupation Rate Evolution On Period"
    Then I see an error message saying "The fields can't be empty"

  Scenario: An authenticated user wants to see graphical information with invalid date
    Given I am an authenticated user
    When I press the menu option "Occupation Rate During Time"
    And I insert the initial date value "12/12/2018"
    And I insert the final date value "32/12/2018"
    And I press the button with text "Show Occupation Rate Evolution On Period"
    Then I see an error message saying "The initial and final dates must be valid and in format dd/mm/yyyy"

  Scenario: An authenticated user wants to see graphical information with invalid format date
    Given I am an authenticated user
    When I press the menu option "Occupation Rate During Time"
    And I insert the initial date value "12-12/2018"
    And I insert the final date value "12/12/2018"
    And I press the button with text "Show Occupation Rate Evolution On Period"
    Then I see an error message saying "The initial and final dates must be valid and in format dd/mm/yyyy"

  Scenario: An authenticated user wants to see graphical information with invalid format date with letters
    Given I am an authenticated user
    When I press the menu option "Occupation Rate During Time"
    And I insert the initial date value "12/mm/2018"
    And I insert the final date value "12/12/2018"
    And I press the button with text "Show Occupation Rate Evolution On Period"
    Then I see an error message saying "The initial and final dates must be valid and in format dd/mm/yyyy"

  Scenario: An authenticated user wants to see graphical information with empty start date
    Given I am an authenticated user
    When I press the menu option "Occupation Rate During Time"
    And I insert the final date value "12/12/2018"
    And I press the button with text "Show Occupation Rate Evolution On Period"
    Then I see an error message saying "The fields can't be empty"

  Scenario: An authenticated user wants to see graphical information with empty final date
    Given I am an authenticated user
    When I press the menu option "Occupation Rate During Time"
    And I insert the initial date value "12/12/2018"
    And I press the button with text "Show Occupation Rate Evolution On Period"
    Then I see an error message saying "The fields can't be empty"

  Scenario: An authenticated user wants to see graphical information with initial date greater than the final date
    Given I am an authenticated user
    When I press the menu option "Occupation Rate During Time"
    And I insert the initial date value "13/12/2018"
    And I insert the final date value "12/12/2018"
    And I press the button with text "Show Occupation Rate Evolution On Period"
    Then I see an error message saying "The initial date can not be higher or equal to the final date"

  Scenario: An authenticated user wants to see graphical information with date from the future
    Given I am an authenticated user
    When I press the menu option "Occupation Rate During Time"
    And I insert the initial date from the future
    And I insert the final date higher than initial date
    And I press the button with text "Show Occupation Rate Evolution On Period"
    Then I see an error message saying "The date must not be higher or equal to the actual date"

  Scenario: An authenticated user wants to see graphical information whenthere is no data registered
    Given I am an authenticated user
    When I press the menu option "Occupation Rate During Time"
    And I insert the initial date value "11/11/2016"
    And I insert the final date value "12/12/2016"
    And I press the button with text "Show Occupation Rate Evolution On Period"
    Then I see an error message saying "No data found for given time period"

