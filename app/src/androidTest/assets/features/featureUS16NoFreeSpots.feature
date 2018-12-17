Feature: Park manually
  As an authenticated user I want to chose a spot on the dashboard and me able to spot there

  Scenario: No free spots available
    Given I am authenticated user
    When I select the Park D
    When I press the "Park Manually" on the menu
    Then I see a dialog box saying that there's no free spots