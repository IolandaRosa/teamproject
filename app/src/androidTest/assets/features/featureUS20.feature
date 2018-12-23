# new feature
# Tags: optional

Feature: An authenticated user can see the statistics dashboard total with correct values displayed

  Scenario: An authenticated user can see the values displayed
    Given I am an authenticated user
    When I press the menu option "Statistics" on dashboard auth screen
    Then I see the statistic screen displayed
    And I confirm that the value of registered users is at least 1
    And The value of logged users is at least 1
    And The value of total occupied spots is at least 0
    And The top 5 most parking and best rated spots is displayed