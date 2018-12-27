Feature: Incident Reports list and details

  Scenario: User wants to details of a incident report
    Given I am an authenticated user
    When I press the menu option "Incidents Reports List"
    And I see a list displayed with the part of the description and location of the incident
    And I press a position on the list
    Then I am on the report details screen
    And I see the total description and location of report displayed