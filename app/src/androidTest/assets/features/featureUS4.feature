Feature: Dashboard for authenticated users

  Scenario: An authenticated user can see the dashboard for authenticated users
    Given I am an anonimous user
    When I login in the application
    And I see the authenticated user dashboard
    And I see the free spots markers on the map
    And I see the information of total free spots, total of ocupied spots, and the date of update
    And I see the hamburger button for the menu 