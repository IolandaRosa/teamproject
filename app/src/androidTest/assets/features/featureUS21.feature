# new feature
# Tags: optional

Feature: An authenticated user can see the performance medium time
  and the occupation rate of the parks at the current moment

  Scenario: An authenticated user can see the information from the dashboard authentication menu
    Given I am an authenticated user
    When I press the menu option "Algorithm Performance"
    Then I am in the statistics screen
    And I see the performance time and statistics displayed

  Scenario Outline: An authenticated user can see the information from the other activities
    Given I am an authenticated user
    When I press the <menu_option>
    And I see the button "View Occupation Rate And Find Me a Spot Performance"
    And I press the button
    Then I am in the statistics screen
    And I see the performance time and statistics displayed

    Examples:
      | menu_option        |
      | Profile            |
      | Statistics         |
      | Change my password |

  Scenario: An authenticated user can see the information in the update profile activity
    Given I am an authenticated user
    When I press the "Profile"
    And I see the button "Update my profile"
    And I press that button
    And I see the button "View Occupation Rate And Find Me a Spot Performance"
    And I press the button
    Then I am in the statistics screen
    And I see the performance time and statistics displayed
